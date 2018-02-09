# Getting started
## 計測APIサーバ
(1) Start database on docker
```text
$ docker-compose up -d
```
(2) Start class `Application` with Java 8.

## クライアント
(1) Download `client/metrixir.client.js` to your code.

```html
<script src="/js/metrixir.client.js"></script>

<script>
    METRIXIR.server.host = '${計測サーバのhost:"localhost:3001"}';
</script>
```
(2) Added class `metrixir` in your html.

```html
<div class="metrixir">
    <input type="text" name="hoge" />
    <input type="text" name="hoge" />
    <select name="hoge">
        <option value="1">tokyo</option>
        <option value="2">osaka</option>
    </select>
    <!-- etc. -->
</div>
```

## metrics view
(1) Access to `http://localhost:3001`


# ENV Properties

|properties|detail|default value|
|:---------|:-----|:------------|
|SERVER_DOMAIN|cookieに登録するmetrixirサーバのdomain名|localhost|
|DATASOURCE_URL|DBの接続先URL|jdbc:postgresql://localhost/metrixir|
|DATASOURCE_USERNAME|DBの接続ユーザ名|dba|
|DATASOURCE_PASSWORD|DBの接続パスワード|p@ssw0rd|
|CORS_ORIGIN|オリジン間リソース共有(CROS)時に使用される `Access-Control-Allow-Origin` 値|localhost:3000|

