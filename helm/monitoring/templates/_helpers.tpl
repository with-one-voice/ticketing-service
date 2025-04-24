{{- define "monitoring.annotations" -}}
prometheus.io/scrape: "true"
prometheus.io/port: "{{.port}}"
prometheus.io/path: "/actuator/prometheus"
{{- end}}