// 拖拽上传
var dropClient;
var dropTerm;

function initClient(client, term) {
    dropClient = client;
    dropTerm = term;
}
$("#terminal").on({
    //拖进触发的事件
    "dragenter": function (e) {
        //清除浏览器默认的功能
        e.preventDefault();
        console.log("有东西在拖动")
    },
    //在目标地点拖动触发的事件
    "dragover": function (e) {
        //清除浏览器默认的功能
        e.preventDefault();
        console.log("文件进来了")
    },
    //在目标地点放置后出发的事件
    "drop": function (e) {
        //清除浏览器默认的功能
        e.preventDefault();
        //获取文件的信息，可自行输出查看
        let file = e.originalEvent.dataTransfer.files;
        console.log(file);
        //将获取的文件信息替换到input的自带属性的files中
        $("#file").prop("files",file[0]);

        // 向 FormData 中追加文件
        var fd = new FormData()
        fd.append('files', file[0]);

        if (dropClient) {
            $.ajax({
                method: 'POST',
                url: '/api/upload/files',
                data: fd,
                // 不修改 Content-Type 属性，使用 FormData 默认的 Content-Type 值
                contentType: false,
                // 不对 FormData 中的数据进行 url 编码，而是将 FormData 数据原样发送到服务器
                processData: false,
                success: function(res) {
                    console.log(res);
                    callback(res);
                }
            })
        }



        function callback(res) {
            $(res).each((e, value) => {
                dropClient.sendClientData("\rfile:"+value);
                dropClient.sendClientData("\r");
                dropClient.sendClientData("\rll /home");
                dropClient.sendClientData("\r");
            })

            console.log('回调成功', res)
        }
    }
})