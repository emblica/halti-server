from collections import defaultdict
from itertools import cycle
import random, copy

hosts = [
    {"name": "A", "cpu": 2, "memory": 1000},
    {"name": "B", "cpu": 1, "memory": 2000},
    {"name": "C", "cpu": 2, "memory": 4000},
]


apps = [
    {"name": "App A", "cpu": 0.8, "memory": 500, "instances": 3},
    {"name": "App B", "cpu": 0.1, "memory": 200, "instances": 2},
    {"name": "App C", "cpu": 1, "memory": 800, "instances": 1},
]

containers = []

## Container flatmap
for app in apps:
    for instance_id in range(app['instances']):
        instance = {"name": app['name'], "cpu": app['cpu'], "memory": app['memory'], "instance_id": instance_id}
        containers += [instance]



def random_config():
    h = copy.deepcopy(hosts)

    config = []
    container_count = sum([app['instances'] for app in apps])
    config = [-1] * container_count
    # Markov-chain like structuring for config
    for container_id in range(container_count):
        container = containers[container_id]
        cpu = container['cpu']
        memory = container['memory']
        for host in h:
            if host['cpu'] - cpu >= 0 and host['memory'] - memory >= 0:
                host['cpu'] -= cpu
                host['memory'] -= memory
                config[container_id] = host['name']

    return config


def score_config(config):
    score = 0
    host_apps = [set()]*len(hosts)
    host_cpu_resources = defaultdict(int)
    host_memory_resources = defaultdict(int)
    container_count = len(containers)

    for container_id in range(container_count):
        host_id = config[container_id]
        container = containers[container_id]
        if host_id == -1:
            score -= 1
        else:
            score += 1
            # If same app is already running in server -1
            if container['name'] in host_apps[host_id]:
                score -= 1
            else:
                host_apps[host_id].update(container['name'])
                score += 1
            host_cpu_resources[host_id] += container['cpu']
            host_memory_resources[host_id] += container['memory']

    for host_id in range(len(host_cpu_resources)):
        host = hosts[host_id]
        if host['cpu'] <= host_cpu_resources[host_id]:
            score += 1
        else:
            score -= 2

        if host['memory'] <= host_memory_resources[host_id]:
            score += 2
        else:
            score -= 3

    return score

def random_modification(config):
    a = random.randint(0, len(containers)-1)
    b = random.randint(0, len(config)-1)

    a_v = config[a]
    config[a] = config[b]
    config[b] = a_v
    return config

def simulated_annealing(t, config):
    score = score_config(config)

    modified = random_modification(config)
    m_score = score_config(modified)

    if m_score > score:
        return modified
    if (random.random() < t):
        return modified
    else:
        return config



def print_config(config):
    container_count = len(containers)
    for container_id in range(container_count):
        host_id = config[container_id]
        container = containers[container_id]['name'] + ':' + str(containers[container_id]['instance_id'])
        host = ""
        if host_id == -1:
            host = "---"
        else:
            host = hosts[host_id]['name']
        print(container, "-->", host)


c = random_config()
best = c
best_score = -1000000
cooldown = 0.8
temp = 1.0
while temp > 0.000001:
    for i in range(10):
        c = simulated_annealing(temp, c)
        score = score_config(c)
        if score > best_score:
            best_score = score
            best = c
            print(best_score)
    temp *= cooldown

print_config(best)
print(best_score)
