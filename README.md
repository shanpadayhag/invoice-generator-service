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

```
