Create .env and fill in the values

```sh
cp .env.example .env
```

To run the code

```sh
set -a; source .env; set +a \
  && mvn spring-boot:run
```

To clear the stuffs

```sh
unset $(grep -v '^#' .env | cut -d '=' -f 1)
```

To build the code

```sh
mvn clean install \
  && docker build --platform linux/amd64 -t arliimainbe . \
  && docker tag arliimainbe:latest shanpadayhag/arliimainbe \
  && docker push shanpadayhag/arliimainbe
```
