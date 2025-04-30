# Monitoring Stack (Grafana + Prometheus + Loki)

## 구성 요소

- Prometheus
- Grafana (Spring Boot, K8s, Loki 대시보드 포함)
- Loki + Promtail
- Helm 기반 배포

## 설치 방법

```bash
helm install kube-prometheus-stack prometheus-community/kube-prometheus-stack \
  -n monitoring -f helm/monitoring/grafana-values.yaml

helm install loki grafana/loki-stack \
  -n monitoring -f helm/monitoring/loki-values.yaml
```

## Grafana 외부 접속 (LoadBalancer 설정)

```bash
kubectl apply -f helm/monitoring/service-grafana-lb.yaml
kubectl get svc -n monitoring kube-prometheus-stack-grafana-lb
```

- EXTERNAL-IP 확인 후 접속: `http://<EXTERNAL-IP>`
- 로그인: `admin / admin`

## 기본 제공 대시보드

- Spring Boot Metrics (Micrometer) - ID: 10280
- Kubernetes Cluster Monitoring - ID: 6417 또는 315
- Prometheus Stats Overview - ID: 3662
- Loki Logs Explore - ID: 13639

## Spring Boot 연동 설정

```yaml
management:
  endpoints:
    web:
      exposure:
        include: prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

## 로그 확인 방법 (Grafana → Explore)

- Datasource: `Loki` 선택
- `{namespace="default"}` 또는 `{app="show-service"}` 등 쿼리로 검색