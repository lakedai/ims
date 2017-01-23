$(document).ready(function(){
	
	$($('#tree11memberwrap')).css({
		'left': $('#11memberbranch').position().left, 
		'top': $('#11memberbranch').position().top + 23, 
		'width': $('#11memberbranch').width() + 4
	});
	$('#container').click(function(){
		if ($('.treewrap1').is(':visible')) {
			$('.treewrap1').hide();
		}
		return true;
	});
	$('.treeedit1').click(function(){
		var tw = $(this).parent().children('.treewrap1');
		if ($(tw).is(':visible')) {
			$(tw).hide();
		}
		else {
			$(tw).show();
		}
		return false;
	});
	$('.treewrap1').click(function(){
		return false;
	});
	$('#save11member').click(function() {
		var data = {
				memberaccount: $('#11memberaccount').val(),
				membermobile: $('#11membermobile').val(),
				memberfullname: $('#11membername').val(), 
				memberbranchid: $('#11memberbranchid').val(), 
				membersex: $('#11membersex').val(),
				memberpositionid: $('#11memberpositionid').val(),
				memberemail: $('#11memberemail').val(),
				memberroleid: $('#11memberroleid').val(),
				memberintro: $('#11memberintro').val(),
			};
		callajax('branch!saveMember', data, cb_11_save_member);
	});
})
function cb_11_save_member(data) {
	if (data.memberid == '0') {
		alert('帐号已存在，请重新输入.');
	}
	else {
		alert('添加成功.');
		callajax("branch!getOrganTree", "", cb_11_tree);
		if ($('#11membercontinue').prop('checked') == false) {
			$('#member').modal('hide');
			if (fillmember == 1) {
				fillmember = 0;
				$('#branchmanager').val($('#11membername').val());
				$('#branchmanagerid').val(data.memberid);
				var t = $.fn.zTree.getZTreeObj('tree110');
				var ns = t.getNodesByParam('id', data.memberid, null);
				t.selectNode(ns[0]);
			}
			if (fillmember == 2) {
				fillmember = 0;
				$('#11branchmanager').val($('#11membername').val());
				$('#11branchmanagerid').val(data.memberid);
				var t = $.fn.zTree.getZTreeObj('tree11branchmanager');
				var ns = t.getNodesByParam('id', data.memberid, null);
				t.selectNode(ns[0]);
			}
		}
		$('#11membermobile').val('');
		$('#11membersex').val('');
		$('#11memberemail').val('');
		$('#11memberintro').val('');
		$('#11memberaccount').val('');
		$('#11membername').val('');
	}
}