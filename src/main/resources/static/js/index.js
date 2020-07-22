(function () {
    /**
     * toast
     * @param content
     * @param icon
     */
    function toast(content) {
        if ($('.toast')) {
            $('.toast').remove();
        }
        var _div = document.createElement('div');
        var _p = document.createElement('p');

        _div.className = 'toast'
        _p.innerText = content;

        _div.appendChild(_p);
        document.body.appendChild(_div);

        setTimeout(() => {
            document.body.removeChild(_div);
        }, 2000);
    }

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


    var maxNum = 0;
    var commitFlag = true;

    $('.number-box').find('.num-add').click(function () {
        var numInput = $(this).parent().find('.num-input');
        numInput.val(Number(numInput.val()) + 1)
    })

    $('.number-box').find('.num-reduce').click(function () {
        var numInput = $(this).parent().find('.num-input');
        if (numInput.val() == 0) {
            return
        }
        numInput.val(Number(numInput.val()) - 1)
    })

    $('input[name=num]').change(function () {
        maxNum = $(this).val()
    })

    $('.commit').click(function () {
        if (!commitFlag) {
            toast("已经点过了，如果不合适重新点开链接再点餐")
            return;
        }
        var peopleNum = $('.peopleNum').val(); //默认人数
        var meatNum = $('.meatNum').val(); //默认荤菜
        var cheapNum = $('.cheapNum').val(); //默认素菜
        var soupNum = $('.soupNum').val(); //默认汤

        $.ajax({
            url: '/diancan/send',
            type: 'post',
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                peopleNum: peopleNum,
                meatNum: meatNum,
                cheapNum: cheapNum,
                soupNum: soupNum,
                maxNum: maxNum
            }),
            success: function () {
                commitFlag = false;
                toast("点餐成功")
            }
        });
    })
})()
