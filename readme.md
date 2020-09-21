#### Compilção

1. Plugin TornadoFX: Vá para **File** -> **Settings** 
-> **Plugins** e selecione o plugin **TornadoFX**.
2. Selecione o projeto principal (*KtLogistic*) e vá para 
**File** -> **Project Structure**.
3. Em **Project Structure** -> **Project**:
    1. Selecione como 
*Project SDK* o SDK correspondente à versão indicada na 
variável no `jdk.version` no `pom.xml`. Então se estiver 
sendo indicada a versão 1.8, você deve selecionar um SDK 
da versão 1.8
    2. Em *Project Language Level* selecione a versão 
correspondente ao SDK selecionado.
4. Em **Project Structure** -> **Modules**:
    1. Clique com o botão direito sobre o projeto 
*ktfractal* e selecione a opção **Add** -> **TornadoFX**. 
Isso fará com que todos os projetos filhos posam rodar as 
aplicações TornadoFX (ver abaixo).
    2. Verifique se o *Language Level* corresponde ao SDK 
selecionado.

#### Execução
1. Abra um projeto e vá para `src` -> `main` -> `kotlin` 
-> `[pacote]` e abre o arquivo com final `View`.
2. Ao abrir, verifique se ao lado da classe `View` aparece 
o ícone do TornadoFX. 
3. Sobre esse ícone, clique com o botão direto do mouse 
e seleciona a opção **Create ???View ...**
4. Ao abrir o diálogo de configuração vá para a aba 
**Configuration**, opção **Use classpath of module**. 
***Verifique que o projeto indicado é o mesmo projeto do 
view que você vai rodar!!***. Se não configurar dessa 
forma vai aparecer a mensagem de erro de que não está 
achando a Classe da view.

#### Relação de projetos e de suas Views
| Projeto                              | Pacote                                     | Classe                    | Observação |
| -------------                        |:-------------                              |:-----                     | -----|
| ktfractal-baker-map                  | `rafael.logitic.maps.baker               ` | `BakerView              ` | |
| ktfractal-bifurcation-canvas         | `rafael.logitic.maps.bifurcation.canvas  ` | `BifurcationView        ` | |
| ktfractal-bifurcation-chart          | `rafael.logitic.maps.bifurcation.chart   ` | `BifurcationView        ` | |
| ktfractal-bifurcation-hiperbolic-tan | `rafael.logitic.maps.hiperbolic_tangent  ` | `HiperbolicTangentView  ` | |
| ktfractal-bifurcation                |                                            |                           | Projeto base para os `ktfractal-bifurcation`|
| ktfractal-core                       |                                            |                           | Projeto Pai de todos |
| ktfractal-duffing-map                | `rafael.logitic.maps.duffing             ` | `DuffingView            ` | |
| ktfractal-gaussian-map               | `rafael.logitic.maps.gaussian            ` | `GaussianView           ` | |
| ktfractal-gingerbreadman-map         | `rafael.logitic.maps.gingerbreadman      ` | `GingerbreadmanView     ` | |
| ktfractal-henon-map                  | `rafael.logitic.maps.henon               ` | `HenonView              ` | |
| ktfractal-julia-set                  | `rafael.logitic.sets.julia               ` | `JuliaSetView           ` | |
| ktfractal-kaplanyorke-map            | `rafael.logitic.maps.kaplanyorke         ` | `KaplanYorkeView        ` | |
| ktfractal-logistic-map               | `rafael.logitic.maps.logistic            ` | `LogisticView           ` | |
| ktfractal-lozi-map                   | `rafael.logitic.maps.lozi                ` | `LoziView               ` | |
| ktfractal-mandelbrot-map             | `rafael.logitic.maps.mandelbrot          ` | `MandelbrotView         ` | |
| ktfractal-mandelbrot-set             | `rafael.logitic.sets.mandelbrot          ` | `MandelbrotSetView      ` | |
| ktfractal-set                        |                                            |                           | Projeto base para `ktfractal-mandelbrot-set`e `ktfractal-julia-set` |
| ktfractal-standard-map               | `rafael.logitic.maps.standard            ` | `StandardView           ` | |
| ktfractal-template                   | `rafael.experimental.template            ` | `TemplateView           ` | |
| ktfractal-tent-map                   | `rafael.logitic.maps.tent                ` | `TentView               ` | |
| ktfractal-tinkerbell-map             | `rafael.logitic.maps.tinkerbell          ` | `TinkerbellView         ` | |
