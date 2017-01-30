var pagenumber = 0;
var curpage = 0;
var itemsperpage = 10;
var itemtemplate='<tr id="traid">'
	+'<td>code</td>'
	+'<td>name</td>'
	+'<td>member</td>'
	+'<td>date</td>'
	+'<td>'
		+'<button onclick="create(aid)" class="btntable" style="margin-right:10px"><img src="images/编辑.png" />修改创建者</button>'
		+'<button onclick="dismiss(aid)" class="btntable"><img src="images/删除.png" />解散群</button>'
	+'</td></tr>';
var grpid = 0;
$(document).ready(function() {
	
	itemsperpage = Math.floor((document.body.clientHeight - 300) / 40);
	var h = document.body.clientHeight - 50;
	$('.sidebar12').css('height', h + 'px');
	$('.menupanel12').css('width', (document.body.clientWidth - 22) * 0.13);

//	curpage = 0;
//	pagenumber = 0;
	callajax('grp!getCount', '', cb_13_count);

	$('#pagefirst').click(function() {
		if (pagenumber == 0) return;
		if (curpage == 0) return;
		curpage = 0;
		loadpage();
	});
	$('#pageprev').click(function() {
		if (pagenumber == 0) return;
		if (curpage == 0) return;
		curpage--;
		loadpage();
	});
	$('#pagenext').click(function() {
		if (pagenumber == 0) return;
		if (curpage + 1 == pagenumber) return;
		curpage++;
		loadpage();
	});
	$('#pagelast').click(function() {
		if (pagenumber == 0) return;
		if (curpage + 1 == pagenumber) return;
		curpage = pagenumber - 1;
		loadpage();
	});

	$('#creator').on('shown.bs.modal', function(e) {
//		curpagec = 0;
//		pagecnumber = 0;
		callajax('grp!getMemberCountByGrp', {id: grpid}, cb_13_creator_count);
	});
});
function cb_13_count(data) {
	pagenumber = Math.ceil(data.id/itemsperpage);
	if (pagenumber > 0) {
		loadpage();
	}
	else {
		$('#grouplist').empty();
		$('#pagecurr').text('0/0');
	}
}
function loadpage() {
	$('#grouplist').empty();
	$('#pagecurr').text((curpage+1) + '/' + pagenumber);
	var data = {page: curpage, itemsperpage: itemsperpage};
	callajax('grp!getList', data, cb_13_fresh);
}
function cb_13_fresh(data) {
	var i = data.length;
	while(i--) {
		$('#grouplist').append(itemtemplate
				.replace('code', data[i].code)
				.replace('name', data[i].name)
				.replace('member', data[i].member)
				.replace('date', showdate(data[i].date))
				.replace(/aid/g, data[i].id)
		);
	}
}
function create(id) {
	grpid = id;
	$('#creator').modal({
		backdrop: false,
		remote: '13_creator.jsp'
	});
}
function dismiss(id) {
	bootbox.confirm({
		'title': '提示',
		'message': '确定解散群么？',
		callback: function(result) {
			if (!result) return;
			callajax('grp!dismiss', {id: id}, cb_13_dismiss);
		}
	});
}
function cb_13_dismiss(data) {
	$('#tr' + data.id).remove();
}