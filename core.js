$(function(){$('select').change(function(){  $("tr").show();
$("tr:not(:contains("+$(this).val()+"))").hide() })});