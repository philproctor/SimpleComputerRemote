package webui

import (
	"net/http"
	"html/template"
)

func handler(w http.ResponseWriter, r *http.Request) {
    t, err := template.New("pagetemplate").Parse(pageTemplate)
    if err == nil {
		t.Execute(w, &pageData{Content: "Systems operating normally."})
	}
}

func Start() {
	http.HandleFunc("/", handler)
    http.ListenAndServe(":11393", nil)
}
