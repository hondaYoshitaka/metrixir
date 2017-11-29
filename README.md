# Getting started
1. Get `metricir.client.js`.
2. Added `jquery` and `metricir.client.js` on your page.
```html
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/js/metrixir.client.js"></script>

<script>
    METRIXIR.server.host = '${計測サーバのhost:"localhost:3001"}';
</script>
```
3. Added class of `metrixir`.
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