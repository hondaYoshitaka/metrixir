# Getting started
## 計測APIサーバ
(1) Start class `Application` with Java 8.

## クライアント
(1) Download `client/metricir.client.js` to your code.

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
(1) Access to `http://localhost:3001/metrics`
