<!doctype html>
<html>
<head>
    <title>WebSSH</title>
    <link rel="stylesheet" href="../css/xterm.css" />
</head>
<body>
<div id="terminal" style="width: 100%;height: 100%"></div>
<input type="hidden" value="${ipnet}" name="ipnet">
<script src="../js/jquery-3.4.1.min.js"></script>
<script src="../js/xterm.js" charset="utf-8"></script>
<script src="../js/webssh.js" charset="utf-8"></script>
<script>
    openTerminal( {
        operate:'connect',
        host: '${ipnet}',//IP
        port: '${port}',//端口号
        username: 'root',//用户名
        password: '${password}'//密码
    });
    function openTerminal(options){
        var client = new WSSHClient();
        var term = new Terminal({
            cols: 97,
            rows: 37,
            cursorBlink: true, // 光标闪烁
            cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
            scrollback: 800, //回滚
            tabStopWidth: 8, //制表宽度
            screenKeys: true
        });

        term.on('data', function (data) {
            //键盘输入时的回调函数
            client.sendClientData(data);
        });
        term.open(document.getElementById('terminal'));
        //在页面上显示连接中...
        term.write('Connecting...');
        //执行连接操作
        client.connect({
            onError: function (error) {
                //连接失败回调
                term.write('Error: ' + error + '\r\n');
            },
            onConnect: function () {
                //连接成功回调
                client.sendInitData(options);
            },
            onClose: function () {
                //连接关闭回调
                term.write("\rconnection closed");
            },
            onData: function (data) {
                //收到数据时回调
                term.write(data);
            }
        });
    }
</script>
</body>
</html>