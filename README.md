# product-stock

## Preparativos
* crie um diretório na sua pasta home chamado *~/data* e um outro chamado *~/data/files*
   Exemplo:
   ```
   > cd ~
   > mkdir data
   > cd data
   > mkdir files
   ```
* mova os arquivos de dados JSON para a pasta *~/data/files*
(OBS: apenas os arquivos *data\*.json* serão processados)


## Premissas e concessões dadas as restrições de tempo:
* O sistema não inclui no banco de dados registros repetidos vindos do mesmo arquivo, 
   porém se o arquivo for alterado estruturalmente o sistema pode entrar em um estado 
   inconsistente. Num projeto mais longo eu: 1) moveria o arquivo para uma pasta de arquivos 
   processados; 2) Criaria uma tabela para acompanhar a situação de cada arquivo com 
   status de PROCESSANDO e FINALIZADO e 3) acrescentaria tamanho e/ou MD5 do arquivo 
   como chave, para que ele não pudesse ser processado novamente;
* A distribuição dos produtos entre os lojistas não é destrutiva, ou seja, você pode 
  executar a mesma operação várias vezes, variando a quantidade para verificar o tipo 
  de distribuição que o sistema gera. Alterar isto para que o produto seja removido 
  da base após a distribuição é bem simples;
* A carga do arquivo é feita sob demanda, para isto basta executar o seguinte comando:
```
> curl -G http://localhost:8080/products/processFiles
```

* Criar um mecanismo *cron* ou utilizar um *quartz* para que o sistema verifique se existem 
arquivos novos para serem carregados é possível, porém para isto seria necessário primeiro fazer 
a movimentação dos arquivos já processados para outra pasta (também é possível, mas 
não foi possível ser feito por conta do tempo alocado para esta tarefa) 
* Criei testes unitários apenas do acesso ao banco de dados, por limitação de tempo. 
Como o projeto ficou divertido eu pretendo extender a cobertura de testes para mais 
partes do código.
  

## Executando a aplicação
Esta é uma aplicação web. Você pode subir o servidor na porta 8080 executando o comando 
na raiz da aplicação:
```
>mvn spring-boot:run
```

#### Carregando os arquivos:

```
> curl -G http://localhost:8080/products/processFiles
```
A carga inicial leva aproximadamente 1 min. Depois disso as chamadas subsequentes fazem 
com que o sistema verifique se os registros já foram inseridos no banco, mas não altera 
os valores caso já tenham sido gravados.



#### Distribuição dos produtos entre os lojistas
A distribuição é feita através de uma chamada REST (GET) informando o produto e a quantidade 
de lojistas que devem receber os produtos. O resultado é um JSON com a seguinte estrutura:
```
[
    {
        "lojista": "Lojista 1",
        "products": [
            {
                "id": 46724,
                "product": "AAME",
                "quantity": 17,
                "price": 2.94,
                "type": "2XL",
                "industry": "Life Insurance",
                "origin": "DC"
            },
            {
                "id": 27155,
                "product": "AAME",
                "quantity": 36,
                "price": 9.84,
                "type": "2XL",
                "industry": "Life Insurance",
                "origin": "OH"
            },
            {
                "id": 44596,
                "product": "AAME",
                "quantity": 42,
                "price": 6.86,
                "type": "XS",
                "industry": "Life Insurance",
                "origin": "TX"
            }
        ],
        "totalValue": 692.34
    },
    {
        "lojista": "Lojista 2",
        "products": [
            {
                "id": 46724,
                "product": "AAME",
                "quantity": 16,
                "price": 2.94,
                "type": "2XL",
                "industry": "Life Insurance",
                "origin": "DC"
            },
            {
                "id": 27155,
                "product": "AAME",
                "quantity": 37,
                "price": 9.84,
                "type": "2XL",
                "industry": "Life Insurance",
                "origin": "OH"
            },
            {
                "id": 44596,
                "product": "AAME",
                "quantity": 42,
                "price": 6.86,
                "type": "XS",
                "industry": "Life Insurance",
                "origin": "TX"
            }
        ],
        "totalValue": 699.24
    }
]
```




Distribuindo o produto EMMS para 2 lojistas:
```
>curl -G http://localhost:8080/products/EMMS/distribute/2
```



Distribuindo o produto WF para 5 lojistas:
```
>curl -G http://localhost:8080/products/WF/distribute/5
```




## Abrindo o console
Foi utilizado o banco de dados H2 com peristência em disco.
Com a aplicaç˜åo rodando, você pode acessar o console em: *http://localhost:8080/h2-console*
 * usuário: sa
 * senha: password 
   
(OBS: o usuário e a senha podem ser alterados no arquivo de propriedades *application.properties*)
    
