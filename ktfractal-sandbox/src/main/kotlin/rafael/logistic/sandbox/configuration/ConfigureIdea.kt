package rafael.logistic.sandbox.configuration

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

const val JAVA_FX_DIR = "C:/dev/javafx-sdk-11.0.2"

const val WORKSPACE_PATH = ".idea/workspace.xml"

private fun pomToModules(currentDir: File): List<String> {
    val pomFile = File(currentDir, "pom.xml")

    // from https://www.baeldung.com/java-xpath
    val builderFactory = DocumentBuilderFactory.newInstance()
    val builder = builderFactory.newDocumentBuilder()
    val xmlDocument: Document = builder.parse(pomFile)
    val xPath: XPath = XPathFactory.newInstance().newXPath()
    val expression = "/project/modules/module"
    val nodes = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET) as NodeList

    return (0..nodes.length)
        .mapNotNull { nodes.item(it) }
        .map { node -> node.textContent }
        .filter { name -> name.count { it == '-' } > 1 }
}

private fun dirToLines(moduleDir: File, moduleName: String): Triple<String, String?, String?> {
    val lines = moduleDir.walk(FileWalkDirection.TOP_DOWN).first { file ->
        file.isFile && file.name.endsWith("View.kt")
    }.readLines()

    val packageLine = lines.firstOrNull { line -> line.startsWith("package rafael.logistic") }
    val appLine = lines.firstOrNull { line -> line.startsWith("class ") }
    return Triple(moduleName, packageLine, appLine)
}

private fun linesToNames(packageLine: String?, appLine: String?, moduleName: String): Triple<String, String, String> {
    // package
    val packageSpacePos = packageLine!!.indexOf(' ')
    val packageName = packageLine.substring(packageSpacePos + 1)

    // app Name
    val appSpacePos = appLine!!.indexOf(' ')
    val appColonPos = appLine.indexOf(':')
    val appName = appLine.substring(appSpacePos, appColonPos).trim()

    return Triple(moduleName, packageName, appName)
}

private fun namesToXml(appName: String, packageName: String, moduleName: String) = """
    <configuration name="$appName" type="TORNADOFX_RUNCONFIGURATION" factoryName="TornadoFX Configuration Factory" run-type="App" live-views="false" live-stylesheets="false" dump-stylesheets="false">
      <option name="MAIN_CLASS_NAME" value="$packageName.$appName" />
      <module name="$moduleName" />
      <option name="VM_PARAMETERS" value="--module-path $JAVA_FX_DIR/lib --add-modules=javafx.controls,javafx.fxml --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED" />
      <method v="2">
        <option name="Make" enabled="true" />
      </method>
    </configuration>
""".trimIndent()

fun createRunManagerNode(doc: Document): Node {
    val node = doc.createElement("component")
    node.setAttribute("name", "RunManager")
    doc.importNode(node, true)
    return doc.firstChild.appendChild(node)
}

fun fillWorkspace(currentDir: File, names: List<Triple<String, String, String>>) {
    val workspaceFile = File(currentDir, WORKSPACE_PATH)

    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val doc = builder.parse(workspaceFile)

    val components = doc.getElementsByTagName("component")
    val runManagerNode: Node = (0..components.length).mapNotNull { components.item(it) }.firstOrNull { node ->
        node.attributes.getNamedItem("name").nodeValue == "RunManager"
    } ?: createRunManagerNode(doc)

    names
        .asSequence()
        .map { (moduleName, packageName, appName) -> namesToXml(appName, packageName, moduleName) }
        .map { StringReader(it) }
        .map { InputSource(it) }
        .map { builder.parse(it) }
        .map { doc.importNode(it.documentElement, true) }
        .forEach { runManagerNode.appendChild(it) }

    val listNode = doc.createElement("list").also { runManagerNode.appendChild(it) }
    names.asSequence().map { (_, _, appName) -> appName }
        .map { "<item itemvalue=\"TornadoFX.$it\" />" }
        .map { StringReader(it) }
        .map { InputSource(it) }
        .map { builder.parse(it) }
        .map { doc.importNode(it.documentElement, true) }
        .forEach { listNode.appendChild(it) }


    doc.documentElement.normalize()
    // https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java?page=2&tab=oldest#tab-top
    // write the content into xml file
    val source = DOMSource(doc)
    val result = StreamResult(workspaceFile)
    val transformerFactory = TransformerFactory.newInstance()
    val transformer: Transformer = transformerFactory.newTransformer()
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    transformer.transform(source, result)
}

/**
 * Deve ser executado toda vez que o projeto acabou de ser baixado do GIT e
 * aberto pela primeira vez pelo IntelliJ ou se o diretório `.idea` for
 * deletado e o IntellJ for aberto depois.
 * Configura as execuções das Classes `*App` sem ter que precisar configurar uma por uma.
 */
fun main() {
    val currentDir = File(".")

    val names = pomToModules(currentDir)
        .asSequence()
        .map { moduleName -> Pair(moduleName, File(currentDir, moduleName)) }
        .map { (moduleName, moduleDir) -> dirToLines(moduleDir, moduleName) }
        .filter { it.second != null && it.third != null }
        .map { (moduleName, packageLine, appLine) -> linesToNames(packageLine, appLine, moduleName) }
        .toList()

    fillWorkspace(currentDir, names)
}

