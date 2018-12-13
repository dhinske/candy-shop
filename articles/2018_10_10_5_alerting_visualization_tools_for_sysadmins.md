# 5 alerting and visualization tools for sysadmins

source: https://opensource.com/article/18/10/alerting-and-visualization-tools-sysadmins?utm_medium=email&utm_source=topic+optin&utm_campaign=awareness&utm_content=20181015+webops+nl

date: 2018-10-10

tags: alerting, sysadmin, visualization, monitoring, open-source

## Alerting
- Alerts in general
  - Alerts should not be sent if the human responder can’t do anything about the problem
  - This leads to alert fatigue and receivers ignoring all alerts within a specific medium until the system escalates to a medium that isn’t already saturated
  - Alerts are not a constant stream of information or a status update.
- Bosun
  - Stack Exchange open-sourced alert management system
  - Supports Graphite, InfluxDB, OpenTSDB, and Elasticsearch
  - Consists of a single server binary, a backend like OpenTSDB, Redis, and scollector agents
  - The scollector agents automatically detect services on a host and report metrics for those processes and other system resources. This data is sent to a metrics backend.
  - The Bosun server binary then queries the backends to determine if any alerts need to be fired.
  - Lets you test your alerts against historical data   
  - Usual features like showing simple graphs and creating alerts
  - Only has email and HTTP notification configurations, which means connecting to Slack and other tools requires a bit more customization
- Cabot
  - It was a Christmas project built because its developers couldn’t wrap their heads around Nagios
  - Similar to Bosun in that it doesn’t collect any data
  - It reaches out into each system’s API and retrieves the information it needs to make a decision based on a specific check
  - Stores the alerting data in a Postgres database and also has a cache using Redis
  - Cabot natively supports Graphite, but it also supports Jenkins, which is rare in this area. (Arachnys (developers of Cabot) uses Jenkins like a centralized cron)
  - Cabot can integrate with Google Calendar for on-call rotations
- StagsAgg
  - Metrics aggregation platform
  - Supports Graphite, StatsD, InfluxDB, and OpenTSDB as inputs or output
  - Written in Java and consists only of the main server and UI, which keeps complexity to a minimum
  - Send alerts based on regular expression matching and is focused on alerting by service rather than host or instance

## Visualization
- Grafana
  - Presenting monitoring data in a more usable and pleasing way
  - Natively gather data from Graphite, Elasticsearch, OpenTSDB, Prometheus, and InfluxDB
  - Integrating alerting and other features that aren’t traditionally combined with visualizations
  - You can set alerts visually and then tell Grafana where to send the alert
  - Collaboration features, like sharing templates and dashboards or annotations ( add context to part of a graph)
- Vizceral
  - Netflix created Vizceral to understand its traffic patterns better when performing a traffic failover.
  - Vizceral serves a very specific use case.
  - It’s worth running it in a demo environment just to better grasp the concepts and witness what’s possible with these systems.
