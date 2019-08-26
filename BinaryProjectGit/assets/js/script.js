
var height;
var width;
if ($(window).width() <= 960 && $(window).height() <= 640) { 
	height = "300px";
	width = "450px";
}else{
	height = "1000px";
	width = "1500px";
}
var graph =   new TradingView.widget(
  {
  	"height": height,
  	"width": width,
  "autosize": true,
  "symbol": "NASDAQ:AAPL",
  "interval": "D",
  "timezone": "Etc/UTC",
  "theme": "Dark",
  "style": "1",
  "locale": "fr",
  "toolbar_bg": "#f1f3f6",
  "enable_publishing": false,
  "allow_symbol_change": true,
  "container_id": "tradingview_6ae6b"
}
  );