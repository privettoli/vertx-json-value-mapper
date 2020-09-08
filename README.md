# vert.x Json Value Mapper
A tiny library to map values in Vert.x JsonObject.

Use-case: having JsonObject, using a regular expression, replace all string values to a particular replacement.

```groovy
implementation 'io.github.privettoli:vertx-json-value-mapper:0.0.1'
```

### Example:
```java
new JsonValueRegexpMapper( <-- not thead-safe object however blocking code in verticle is never executed by multiple threads at the same time
    "^(\\d{4})(\\d{2})(\\d{2})T(\\d{2})(\\d{2})(\\d{2})\\.(\\d{3})\\sGMT$",
    "$1-$2-$3T$4:$5:$6.$7Z",
    "19890918T053347.789 GMT".length()
).map(jsonObject) <-- mutates original JsonObject instance
```
Configuration:
```regexp
^(\\d{4})(\\d{2})(\\d{2})T(\\d{2})(\\d{2})(\\d{2})\\.(\\d{3})\\sGMT$ <-- pattern
```
```regexp
$1-$2-$3T$4:$5:$6.$7Z <-- replacement
```
```java
23 <-- length
```
Input:
```json
{
  "requestedCruise": "YACHT_CLUB_TO_SEYCHELLES",
  "attendants": [
    {
      "bender": -1,
      "riskReason": "OD",
      "riskDateTime": "20200310T164621.150 GMT",
      "good": false
    }
  ]
}
```
Output:
```json
{
  "requestedCruise": "YACHT_CLUB_TO_SEYCHELLES",
  "attendants": [
    {
      "bender": -1,
      "riskReason": "OD",
      "riskDateTime": "2020-03-10T16:46:21.150Z",
      "good": false
    }
  ]
}
```

### Performance:

4 files used for the benchmark: 
- [pegaSmallPayload.json 506 bytes](src/test/resources/pegaSmallPayload.json)
- [iso8601SmallPayload.json 508 bytes](src/test/resources/iso8601SmallPayload.json)
- [pegaBiggerPayload.json 26,085,447 bytes (26.1 MB)](src/test/resources/pegaBiggerPayload.json)
- [iso8601BiggerPayload.json 26,085,626 bytes (26.1 MB)](src/test/resources/iso8601BiggerPayload.json)

Results:

<details>
  <summary>MacBook Pro 2018 i7-8559U</summary>

Intel(R) Core(TM) i7-8559U CPU @ 2.70GHz<br>
16 GB 2133 MHz LPDDR3<br>
macOS Catalina 10.15.6 (19G2021)<br>
>  openjdk 11.0.7 2020-04-14 **HotSpot**<br>
> OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.7+10)<br>
> OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.7+10, mixed mode)

Benchmark | Mode | Cnt | Score | Error | Units
--------- | ------ | -------- | ---- | ------- | ----
FromIso8601ToPegaBenchmark.BiggerPayload.biggerPayload  | thrpt | 25 | 34.053 | ± 0.850 | ops/s
FromIso8601ToPegaBenchmark.SmallJsonPayload.smallJsonPayload | thrpt |  25 | 848.351| ± 19.475 | ops/ms
FromPegaToIso8601Benchmark.BiggerPayload.biggerPayload     |   thrpt |  25 |  34.948| ±  1.148 |  ops/s
FromPegaToIso8601Benchmark.SmallJsonPayload.smallJsonPayload | thrpt |  25 | 729.366| ± 10.665 | ops/ms

>  openjdk 11.0.7 2020-04-14 **GraalVM**<br>
> OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.7+10)<br>
> OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.7+10, mixed mode)<br>
> -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler

Benchmark | Mode | Cnt | Score | Error | Units
--------- | ------ | -------- | ---- | ------- | ----
FromIso8601ToPegaBenchmark.BiggerPayload.biggerPayload       | thrpt  | 25 |  56.085| ±  1.667 |  ops/s
FromIso8601ToPegaBenchmark.SmallJsonPayload.smallJsonPayload | thrpt  | 25 | 968.307| ± 36.925 | ops/ms
FromPegaToIso8601Benchmark.BiggerPayload.biggerPayload       | thrpt  | 25 |  56.756| ±  2.085 |  ops/s
FromPegaToIso8601Benchmark.SmallJsonPayload.smallJsonPayload | thrpt  | 25 | 829.512| ±  9.401 | ops/ms


</details>

<details>
  <summary>Home Server Xeon E3-1270 V2</summary>

Intel(R) Xeon(R) CPU E3-1270 V2 @ 3.50GHz<br>
32 GB 1600 MHz DDR3<br>
FreeBSD 11.3-RELEASE-p7 amd64<br>
> openjdk 11.0.7 2020-04-14 **HotSpot**<br>
> OpenJDK Runtime Environment (build 11.0.7+10-2)<br>
> OpenJDK 64-Bit Server VM (build 11.0.7+10-2, mixed mode)<br>

Benchmark | Mode | Cnt | Score | Error | Units
--------- | ------ | -------- | ---- | ------- | ----
FromIso8601ToPegaBenchmark.BiggerPayload.biggerPayload        | thrpt   | 25  |  42.193 | ±  0.686 |   ops/s
FromIso8601ToPegaBenchmark.SmallJsonPayload.smallJsonPayload  | thrpt   | 25  | 701.996 | ± 20.332 |  ops/ms
FromPegaToIso8601Benchmark.BiggerPayload.biggerPayload        | thrpt   | 25  |  42.240 | ±  0.671 |   ops/s
FromPegaToIso8601Benchmark.SmallJsonPayload.smallJsonPayload  | thrpt   | 25  | 586.037 | ±  9.022 |  ops/ms

> openjdk 11.0.7 2020-04-14 **GraalVM**<br>
> OpenJDK Runtime Environment (build 11.0.7+10-2)<br>
> OpenJDK 64-Bit Server VM (build 11.0.7+10-2, mixed mode)<br>
> -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler

Benchmark | Mode | Cnt | Score | Error | Units
--------- | ------ | -------- | ---- | ------- | ----
FromIso8601ToPegaBenchmark.BiggerPayload.biggerPayload       | thrpt  | 25  | 60.188 | ± 0.582 |  ops/s
FromIso8601ToPegaBenchmark.SmallJsonPayload.smallJsonPayload | thrpt  | 25 | 713.827 | ± 5.949 | ops/ms
FromPegaToIso8601Benchmark.BiggerPayload.biggerPayload       | thrpt  | 25  | 60.906 | ± 0.806 |  ops/s
FromPegaToIso8601Benchmark.SmallJsonPayload.smallJsonPayload | thrpt  | 25 | 596.650 | ± 3.305 | ops/ms
</details>
