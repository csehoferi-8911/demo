# Rövid leírás

Első leadáshoz képest módosult: proyxy pattern, cache annotációval, container kezelés

### Docker parancsok futáshoz

alkalmazás induláskor docker-compose elindul, alkalmazás leállításakor pedig leállnak a containerek is. SpringBootTestnél testcontainereket használok.

### Leírás

application.yml-ben találhatóak a beállítások.

Próbahívások

omdb: curl -X 'GET' \ 'http://localhost:8080/movies/Avengers?api=omdb' \ -H 'accept: */*'

moviedb: curl -X 'GET' \ 'http://localhost:8080/movies/Avengers?api=moviedb' \ -H 'accept: */*'

De van swagger is: http://localhost:8080/swagger-ui/index.html#

Keresés során title és movie api alapján ment adatbázisba és redis cache-be is. Ha már cím megtalálható a memóriában, akkor nem hív be az api-kba, hanem memóriából szedi. 
Ha címre első keresés, akkor megtörténnek az api hívások, db-be és memóriába menté is. Alkalmazás indítása során, db-ben 
lévő filmeket beolvassa a memóriába, ha még a redis nem tartalmazza őket. Idő hiányában, most csak pár unit tesztet írtam.
