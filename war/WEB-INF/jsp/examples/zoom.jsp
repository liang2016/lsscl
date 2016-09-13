<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>

<html>
	<head>
		<meta http-equiv="X-UA-Compatible"
			content="IE=EmulateIE7; IE=EmulateIE9">
		<title>isZoomedIgnoreProgrammaticZoom Flag</title>

		<script type="text/javascript" src="../plugins/excanvas.js"></script>

		<script type="text/javascript" src="../rgbcolor/rgbcolor.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-dev.js"></script>
		<script type="text/javascript" src="../strftime/strftime-min.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-layout.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-canvas.js"></script>
		<script type="text/javascript" src="../plugins/dygraph.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-utils.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-gviz.js"></script>
		<script type="text/javascript"
			src="../plugins/dygraph-interaction-model.js"></script>
		<script type="text/javascript"
			src="../plugins/dygraph-options-reference.js"></script>
				<script type="text/javascript"
			src="../plugins/data.js"></script>
	</head>
	<body>
		<h1>
			实时动态数据
		</h1>
		
	
		<p>
			<input id="isZoomedIgnoreProgrammaticZoom" type="checkbox"
				checked=true />
			Do not change zoom flags (
			<code>
				isZoomedIgnoreProgrammaticZoom
			</code>
			)
		</p>

		<div>
			<div style="float: left">
				<p>
					Max Y Axis:
					<input type="button" value="&uarr;" onclick="adjustTop(+1)"/>
					<input type="button" value="&darr;" onclick="adjustTop(-1)"/>
				</p>
				<p>
					Min Y Axis:
					<input type="button" value="&uarr;" onclick="adjustBottom(+1)"/>
					<input type="button" value="&darr;" onclick="adjustBottom(-1)"/>
				</p>
				<p>
					Min X Axis:
					<input type="button" value="&larr;"
						onclick="adjustFirst(-100000000)"/>
					<input type="button" value="&rarr;"
						onclick="adjustFirst(+100000000)"/>
				</p>
				<p>
					Max X Axis:
					<input type="button" value="&larr;"
						onclick="adjustLast(-100000000)"/>
					<input type="button" value="&rarr;"
						onclick="adjustLast(+100000000)"/>
				</p>
			</div>
			<div id="div_g" style="width: 600px; height: 300px; float: left"></div>
			<div style="float: left">

			</div>
		</div>
		<div style="display: inline-block">
			<h4>
				Zoomed Flags
			</h4>
			<p>
				Zoomed:
				<span id="zoomed">False</span>
			</p>
			<p>
				Zoomed X:
				<span id="zoomedX">False</span>
			</p>
			<p>
				Zoomed Y:
				<span id="zoomedY">False</span>
			</p>
			<h4>
				Window coordinates (in dates and values):
			</h4>
			<div id="xdimensions"></div>
			<div id="ydimensions"></div>
		</div>

		<script type="text/javascript">
	g = new Dygraph(document.getElementById("div_g"), NoisyData, {
		errorBars : true,
		zoomCallback : function(minDate, maxDate, yRange) {
			showDimensions(minDate, maxDate, yRange);
		},
		drawCallback : function(me, initial) {
			document.getElementById("zoomed").innerHTML = "" + me.isZoomed();
			document.getElementById("zoomedX").innerHTML = ""
					+ me.isZoomed("x");
			document.getElementById("zoomedY").innerHTML = ""
					+ me.isZoomed("y");
			var x_range = me.xAxisRange()
			var elem = document.getElementById("xdimensions")
			elem.innerHTML = "dateWindow : [" + x_range[0] + ", " + x_range[1]
					+ "]"
		}
	})

	// Pull an initial value for logging.
	var minDate = g.xAxisRange()[0];
	var maxDate = g.xAxisRange()[1];
	var minValue = g.yAxisRange()[0];
	var maxValue = g.yAxisRange()[1];
	showDimensions(minDate, maxDate, [ minValue, maxValue ]);

	function showDimensions(minDate, maxDate, yRanges) {
		showXDimensions(minDate, maxDate);
		showYDimensions(yRanges);
	}

	function getNoChange() {
		var options = {}
		var elem = document.getElementById("isZoomedIgnoreProgrammaticZoom")
		if (elem.checked) {
			options.isZoomedIgnoreProgrammaticZoom = true
		}
		return options
	}

	function adjustTop(value) {
		options = getNoChange()
		maxValue += value
		options.valueRange = [ minValue, maxValue ]
		console.log(options)
		g.updateOptions(options)
	}

	function adjustBottom(value) {
		options = getNoChange()
		minValue += value
		options.valueRange = [ minValue, maxValue ]
		console.log(options)
		g.updateOptions(options)
	}

	function adjustFirst(value) {
		options = getNoChange()
		minDate += value
		options.dateWindow = [ minDate, maxDate ]
		console.log(options)
		g.updateOptions(options)
	}

	function adjustLast(value) {
		options = getNoChange()
		maxDate += value
		options.dateWindow = [ minDate, maxDate ]
		g.updateOptions(options)
	}

	function showXDimensions(first, second) {
		var elem = document.getElementById("xdimensions");
		elem.innerHTML = "dateWindow: [" + first + ", " + second + "]";
	}

	function showYDimensions(ranges) {
		var elem = document.getElementById("ydimensions");
		elem.innerHTML = "valueRange: [" + ranges + "]";
	}
</script>
	</body>
</html>
