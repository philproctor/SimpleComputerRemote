package webui

type pageData struct {
	Content string
}

const pageTemplate =
`<html lang="en">
<head>
<title>Simple Computer Remote - Administration</title>
<style>
body {background-color:#333;}
div.Rounded {
	-moz-border-radius: 20px;
	-webkit-border-radius: 20px;
	-khtml-border-radius: 20px;
	border-radius: 20px;
	height: 550px;
	width: 700px;
	margin: 0 auto 0 auto;
	background-color: #ccc;
	padding: 10px 10px 10px 10px;
	box-shadow: 4px 4px 5px #000;
	border: solid #000 1px;
	text-align: center;
}
</style>
</head>
<body>

<div class="Rounded">
<h1>Simple Computer Remote</h1><hr /><br />
{{.Content}}
</div>

</body>
</html>`
