# Desafio Técnico

## Como instalar:

1) Fazer o pull do projeto, e na pasta do projeto executar o comando "mvn clean install". 
Será gerado um arquivo "search-1.0.0.jar" na pasta "target". Se preferir, pode fazer o download do arquivo diretamente [aqui](https://github.com/andrevbarbosa/testelabs/blob/master/target/search-1.0.0.jar)
.

2) Copiar o arquivo "search-1.0.0.jar" para a pasta de execução. Por exemplo, copiar para "/users/labs".

3) Fazer o download do arquivo de filmes em 

```
https://s3-sa-east-1.amazonaws.com/luizalabs-tech-challenges/movies.zip
```

4) Descompactar o arquivo de filmes na pasta de execução. Será criado uma nova pasta "data".
Por exemplo, "/users/labs/data".

5) Executar o comando abaixo na pasta de execução, passando como parâmetros as sentenças a pesquisar:

```
java -jar search-1.0.0.jar "sentença1 [sentença2] [sentença3] ... [sentençaN]"
```

Exemplo:

```
/users/labs$ java -jar search-1.0.0.jar "disney walt"
```
