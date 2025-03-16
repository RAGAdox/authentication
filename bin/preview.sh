export $(grep -v '^#' .env | xargs)
./mvnw clean package
java -jar target/*.jar