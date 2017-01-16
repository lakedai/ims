/**
 * Created by zhu_jq on 2017/1/9.
 */
$(function(){
    window.converseACount = [];
    //删除群租种的成员
    $('.selectedList').delegate('.deleteMemberIcon','click',function(){
        var name = $(this).prev().html();
        var memberID = $(this).parent().attr('memberID');
        deleteElement(converseACount,memberID);
        //changeSelected(converseACount);
        changeSelected(converseACount);
        for(var i = 0;i<converseACount.length;i++){
            var memberID = converseACount[i];
            //$('li[id='+memberID+']').find('.dialogCheckBox').click();
        }
    })

    //弹窗中的树形结构的收起展开
    $('.conversWindow').delegate('.dialogCollspan','click',function(){
        $(this).closest('li').next('ul').slideToggle();
        $(this).toggleClass('dialogCollspanC','dialogCollspanO');
    });

    //弹窗中树形结构的选中
    $('.conversWindow').undelegate('.dialogCheckBox','click')
    $('.conversWindow').delegate('.dialogCheckBox','click',function(){
        converseACount = [];
        //bPrivate是私聊还是群聊
        var bPrivate = $(this).parents('.conversWindow').hasClass('privateConvers');
        var id =  $(this).closest('li').attr('id');
        if(bPrivate){//创建个人的聊天页面 单选模式
            var account =  $(this).closest('li').attr('account');
            $('.dialogCheckBox').removeClass('CheckBoxChecked');
            $(this).addClass('CheckBoxChecked');
            converseACount.push(account);
        }else{//创建群组的聊天 多选模式
            //首先自己的选中状态
            //$(this).toggleClass('CheckBoxChecked','dialogCheckBox');
            if($(this).hasClass('CheckBoxChecked')){
                $(this).removeClass('CheckBoxChecked');
                //$(this).addClass('dialogCheckBox');

            }else{
                //$(this).removeClass('dialogCheckBox');
                $(this).addClass('CheckBoxChecked');

            }
            //然后子级的选中状态
            var member = $(this).closest('li').next('ul').find('div.member');
            if(member){
                for(var i = 0;i<member.length;i++){
                    $(member[i]).parent().attr('account');
                }
            }
            if($(this).hasClass('CheckBoxChecked')){//选中的push
                $(this).closest('li').next('ul').find('.dialogCheckBox').addClass('CheckBoxChecked');
            }else{//未选中，移出数组
                $(this).closest('li').next('ul').find('.dialogCheckBox').removeClass('CheckBoxChecked');
            }
            //父级的选中状态
            var sonBox = $(this).closest('ul').find('.dialogCheckBox');
            var allBox = 0;
            for(var i = 0;i<sonBox.length;i++){
                if($(sonBox[i]).hasClass('CheckBoxChecked')){
                    allBox++;
                }
            }
            if(allBox==0){//全没选中
                $(this).closest('ul').prev('li').find('.chatLeftIcon').removeClass('CheckBoxChecked');
            }else if(allBox==sonBox.length){//全选中
                $(this).closest('ul').prev('li').find('.chatLeftIcon').removeClass('CheckBoxChecked');
            }
            var dialogCheckBox = $('.contactBox').find('.dialogCheckBox');

            for(var i = 0;i<dialogCheckBox.length;i++){
                var diacjeck = $(dialogCheckBox[i])
                if(diacjeck.hasClass('CheckBoxChecked')&&diacjeck.closest('div').hasClass('member')){
                    var account = diacjeck.closest('li').attr('id');
                    var name = diacjeck.next().html();
                    converseACount.push(account);
                }
            }
            changeSelected(converseACount);

        }
    })

})

//删除数组中的某个对象
function deleteElement(arr,name,value){
    for(var i = 0;i<arr.length;i++){
        var curObj = arr[i];
        for(var key in curObj){
            if(curObj[key]==value&&key==name){
                deleteElement(arr,curObj);
            }
        }
    }
    return curObj;
}

//修改select里面的成员
function changeSelected(converseACount){
    var dom = $('.selectedList ul');
    var sHTML = '';
    for(var i = 0;i<converseACount.length;i++){
        var name = findMemberInList(converseACount[i]).name;
        sHTML+='<li memberID="'+converseACount[i]+'"><span class="memberName">'+name+'</span><span class="chatLeftIcon deleteMemberIcon"></span></li>'
    }
    dom.html($(sHTML));
}


function deleteElement(converseACount,account){
    console.log(converseACount);
    for(var i = 0;i<converseACount.length;i++){
        if(converseACount[i]==account){
            converseACount.splice(i,1);
            break;
        }
    }
}