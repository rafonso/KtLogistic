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
2. Ao abrir, verifique se ao lado da classe `App` aparece 
o ícone do TornadoFX. 
3. Sobre esse ícone, clique com o botão direto do mouse 
e seleciona a opção **Create ???App ...**
4. Ao abrir o diálogo de configuração vá para a aba 
**Configuration**, opção **Use classpath of module**. 
***Verifique que o projeto indicado é o mesmo projeto do 
view que você vai rodar!!***. Se não configurar dessa 
forma vai aparecer a mensagem de erro de que não está 
achando a Classe da view.

#### Relação de projetos e de suas Views
| Projeto                               | Pacote                                            | Classe                            | Observação |
| -------------                         |:-------------                                     |:-----                             | -----|
| ktfractal-core                        |                                                   |                                   | Projeto Pai de todos                         |
| ktfractal-bifurcation                 |                                                   |                                   | Projeto base para os `ktfractal-bifurcation` |
| ktfractal-bifurcation-gaussian        | `rafael.logistic.bifurcation.gaussian`            | `GaussianBifurcationApp`          |                                              |
| ktfractal-bifurcation-henon           | `rafael.logistic.bifurcation.henon`               | `HenonBifurcationApp`             |                                              |
| ktfractal-bifurcation-hiperbolic-tan  | `rafael.logistic.bifurcation.hiperbolic_tangent`  | `HiperbolicTangentBifurcationApp` |                                              |
| ktfractal-bifurcation-ikeda           | `rafael.logistic.bifurcation.ikeda`               | `IkedaBifurcationApp`             |                                              |
| ktfractal-bifurcation-logistic        | `rafael.logistic.bifurcation.logistic`            | `LogisticBifurcationApp`          |                                              |
| ktfractal-bifurcation-lozi            | `rafael.logistic.bifurcation.lozi`                | `LoziBifurcationApp`              |                                              |
| ktfractal-bifurcation-tent            | `rafael.logistic.bifurcation.tent`                | `TentBifurcationApp`              |                                              |
| ktfractal-map-baker                   | `rafael.logistic.map.baker`                       | `BakerMapApp`                     |                                              |
| ktfractal-map-duffing                 | `rafael.logistic.map.duffing`                     | `DuffingMapApp`                   |                                              |
| ktfractal-map-gaussian                | `rafael.logistic.map.gaussian`                    | `GaussianMapApp`                  |                                              |
| ktfractal-map-gingerbreadman          | `rafael.logistic.map.gingerbreadman`              | `GingerbreadmanMapApp`            |                                              |
| ktfractal-map-henon                   | `rafael.logistic.map.henon`                       | `HenonMapApp`                     |                                              |
| ktfractal-map-ikeda                   | `rafael.logistic.map.ikeda`                       | `IkedaMapApp`                     |                                              |
| ktfractal-map-kaplanyorke             | `rafael.logistic.map.kaplanyorke`                 | `KaplanYorkeMapApp`               |                                              |
| ktfractal-map-logistic                | `rafael.logistic.map.logistic`                    | `LogisticMapApp`                  |                                              |
| ktfractal-map-lozi                    | `rafael.logistic.map.lozi`                        | `LoziMapApp`                      |                                              |
| ktfractal-map-mandelbrot              | `rafael.logistic.map.mandelbrot`                  | `MandelbrotMapApp`                |                                              |
| ktfractal-map-standard                | `rafael.logistic.map.standard`                    | `StandardMapApp`                  |                                              |
| ktfractal-map-tent                    | `rafael.logistic.map.tent`                        | `TentMapApp`                      |                                              |
| ktfractal-map-tinkerbell              | `rafael.logistic.map.tinkerbell`                  | `TinkerbellMapApp`                |                                              |
| ktfractal-set                         |                                                   |                                   | Projeto base para `ktfractal-set`            |
| ktfractal-set-julia                   | `rafael.logistic.set.julia`                       | `JuliaSetApp`                     |                                              |
| ktfractal-set-mandelbrot              | `rafael.logistic.set.mandelbrot`                  | `MandelbrotSetApp`                |                                              |
| ktfractal-template                    | `rafael.experimental.template`                    | `TemplateApp`                     |                                              |
