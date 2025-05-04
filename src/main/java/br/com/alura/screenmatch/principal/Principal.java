package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import javax.crypto.spec.PSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);

    //final indica que não vamos modifica-la
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=29b70ce6" ;

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public  void exibeMenu(){
        System.out.println("Digite o nome da série para buscar");
        var nomeSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();


        for(int i = 1; i < dadosSerie.totalTemporadas(); i++){
            json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(
                    " ","+") + "&season=" + i
                    +  API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

   /*
       for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
            List<DadosEpisodios> episodiosTemporada = temporadas.get(i).episodios();
            for(int j = 0; j < episodiosTemporada.size(); j++){
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }
        Ao invés de utilizar 2 for, posso utilizar a espressão lambda abaixo
        */

//   lambda:  Temporada( (parametro) -> expressão) / parametro = representação de Temporada
        temporadas.forEach(t -> t.episodios()
                .forEach(e -> System.out.println(e.titulo())));
        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        // .toList();  Lista imutavel
/*
        System.out.println("\n Top 10 eps:");

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek( e -> System.out.println("Primerio filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenação " + e))
                .limit(10)
                .peek(e -> System.out.println("Limite " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Mapeamento " + e) ) //permite observar o que está acontecendo em cada etapa da nossa stream.
                .forEach(System.out::println);
*/
       System.out.println("\n Ep de cada temporada:");
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
/*
        System.out.println("\nQual ep você quer encontrar ");
        var trechoTitulo = scanner.nextLine();
        //Optional é um objeto contêiner que pode ou não conter um valor não nulo
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase())) //contains pega elementos parecidos ou com um trcho
                .findFirst();
        if(episodioBuscado.isPresent() ) {//Pergunta se ela existe
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
            System.out.println("Episódio: " + episodioBuscado.get().getNumeroEpisodio());
        } else {
            System.out.println("Episódio não encontrado!");
        }*/

/*
        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = scanner.nextInt();
        scanner.nextLine(); //temos que colocar um next line para não misturar dados na proxima leitura

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null &&
                        e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data Lançamento: " + e.getDataLancamento().format(formatadorData)
                ));
*/

/*    Streams é uma forma de trabalhar com coleções de dados, sendo uma sequência de elementos que
       pode ser processada em paralelo ou em série.

        List<String> nomes = Arrays.asList("Carlos", "David", "Julio", "Claudio");

        nomes.stream()
                .sorted()       //Operação intermediária gera novo fluxo de dados
                .limit(3)       //Operação intermediária gera novo fluxo de dados
                .filter(n -> n.startsWith("D"))       //Operação intermediária gera novo fluxo de dados
                .map(n -> n.toUpperCase())       //Operação intermediária gera novo fluxo de dados
                .forEach(System.out::println);       //Operação final
*/

    Map<Integer, Double> avaliacosPorTemporada = episodios.stream() // fluxo (stream) a partir da coleção episodios
            .filter(e -> e.getAvaliacao() > 0.0)
            /*O collect é uma operação terminal que transfoma o fluo em uma coleção*/
            .collect(Collectors.groupingBy( /*esse coletor agrupa os elementos com base em uma chave*/
                  /*chave:*/ Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao) ));
                 /*calcula a média das avaliações que foram agrupados */
        System.out.println(avaliacosPorTemporada);
    }
}
