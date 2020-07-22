(function () {
    setInterval(function () {
        var hours = new Date().getHours();
        var minutes = new Date().getMinutes();
        if (hours < 10) {
            hours = '0' + hours;
        }
        if (minutes < 10) {
            minutes = '0' + minutes;
        }
        $('.time').text(+hours + ':' + minutes)
    }, 1000)

    $('.back').click(function () {
        window.location.href = '/diancan'
    })

    $('.checkbox').click(function () {
        var show = 0;
        if(this.checked){
            show = 1
        }
        $.ajax({
            url:'/diancan/api/updateFoodsByFid',
            type: 'post',
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                fid: Number($(this).val()),
                show: show
            }), success: function (res) {
                console.log(res)
            }
        })
    })
})()