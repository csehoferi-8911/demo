# Rövid leírás

### Docker parancsok futáshoz

docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=moviedb -e MYSQL_USER=user -e MYSQL_PASSWORD=password -p 3306:3306 -d mysql:8.0
  
docker run --name redis-cache -p 6379:6379 redis

### Leírás

application.yml-ben találhatóak a beállítások.

Próbahívások

omdb: curl -X 'GET' \ 'http://localhost:8080/movies/Avengers?api=omdb' \ -H 'accept: */*'

moviedb: curl -X 'GET' \ 'http://localhost:8080/movies/Avengers?api=moviedb' \ -H 'accept: */*'

De van swagger is: http://localhost:8080/swagger-ui/index.html#

Keresés során title és movie api alapján ment adatbázisba és redis cache-be is. Én most nem Cache annotációkat használtam,
hanem manuálisan a redisTemplate-t. Ha már cím megtalálható a memóriában, akkor nem hív be az api-kba, hanem memóriából szedi. 
Ha címre első keresés, akkor megtörténnek az api hívások, db-be és memóriába menté is. Alkalmazás indítása során, db-ben 
lévő filmeket beolvassa a memóriába, ha még a redis nem tartalmazza őket. Idő hiányában, most csak pár unit tesztet írtam.
