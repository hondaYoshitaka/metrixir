# Getting started
## 計測APIサーバ
1. Change directory to `api` .
2. Start `example.metrixir.Application` with Java 8.

## クライアント
1. Download `metricir.client.js`.
2. Added `metricir.client.js` on your html.
```html
<script src="/js/metrixir.client.js"></script>

<script>
    METRIXIR.server.host = '${計測サーバのhost:"localhost:3001"}';
</script>
```
3. Added class `metrixir` in your html.

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
1. Access to `http://localhost:3001/metrics`
