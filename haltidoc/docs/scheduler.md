## Scheduling in Halti

Scheduler is pure function described in `halti-server.scheduler`


Scheduling will take hosts and services in creation order in and schedule them with following rules:

1. Multiple each service by it's replication factor _(instances)_
2. Make sure no single host has two copies of the same service running
3. Make sure that the host has **capabilities** the service is **requiring**
4. Make sure we don't overprovision hosts **memory** (so that the service has as much as memory it requires)
5. Make sure we don't overprovision hosts **cpu** (so that the service has as much as CPU it requires)


Scheduler will run each of those services and try to assign all of them to each node and if it will find suitable one it will **assign** it to that instance. Otherwise the scheduler is trying to report to reason why some certain service instance isnt scheduled.
