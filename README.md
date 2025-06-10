# EchoPulse - Distributed Discord Clone

**EchoPulse** est un clone minimaliste distribué de Discord conçu comme démonstrateur technique backend.

## Avertissement

Ce projet **n’a pas vocation à être utilisé en production**. Il sert exclusivement à :

- pratiquer la **conception hexagonale** et la séparation claire des responsabilités,
- manipuler **Kafka**, **Redis**, **WebSocket STOMP** et **OAuth2** dans un contexte distribué,

Aucune attention n’a été portée à l’industrialisation, à la sécurité avancée ou à la couverture de test.

## Avertissement sur le front-end

Le front-end est volontairement minimal. Il ne sert qu'à illustrer les fonctionnalités côté client :

- aucune attention portée à l’UX, la qualité du code ou la sécurité ;
- aucune logique critique n’est implémentée ;
- aucune maintenance prévue.

Le cœur du projet réside entièrement dans l’**infrastructure back-end distribuée**.

---

## Architecture

Le projet EchoPulse est basé sur une **architecture microservices hexagonale** orientée événement, conçue pour démontrer la conception d’un système **temps réel distribué** avec **Kafka** et **WebSocket**.
Le front consomme des APIs REST ou se connecte aux WebSocket.

---

### Vue d’ensemble

Chaque microservice est indépendant, isolé par sa propre base de données et communique via des **messages Kafka** pour garantir le découplage.  
Le front-end communique avec le système via REST et WebSocket (STOMP).

        +-----------------+          Kafka          +----------------+
        |  Server Service |  <------------------->  |  Chat Service  |
        |   (REST + JPA)  |                         | (WebSocket + Redis) |
        +--------+--------+                         +--------+-------+
                 ^                                             ^
                 |                                             |
                 | REST (HTTP)                        WebSocket (STOMP)
                 |                                             |
          +------+-------+                            +--------+--------+
          |   Front-End   | <-----------------------> |      Client      |
          +--------------+                            +------------------+


---


### Services

#### 1. Server Service

- Création et gestion de serveurs, channels, membres
- Publication d’événements Kafka
- Persistance : PostgreSQL
- Communication : REST + Kafka (producer)

#### 2. Chat Service

- Consommation des événements Kafka (channels, messages, users)
- Diffusion via WebSocket STOMP
- Persistance volatile : Redis
- Communication : WebSocket + REST (récupération des messages)

---

### Authentification

- Fournie par Keycloak (OIDC)
- JWT décodés pour chaque appel (REST et WebSocket)
- Authentification intégrée dans le handshake WebSocket et interceptors STOMP

---

### Hexagonal Design

- `core/` : logique métier pure, sans dépendance
- `port in` : interfaces de cas d’usage (commandes entrantes)
- `port out` : interfaces des dépendances externes (Kafka, Redis, WebSocket)
- `adapters/` : implémentations concrètes (Kafka producer, WebSocket handler, etc.)

---

### Événements Kafka

| Événement               | Emetteur | Récepteur |
|------------------------|----------|-----------|
| channels.create        | server   | chat      |
| channels.delete        | server   | chat      |
| user.join-server       | server   | chat      |
| user.leave-server      | server   | chat      |
| channels.send-message  | chat     | chat      |

---

## Stack technique

| Domaine         | Outils                  |
|-----------------|-------------------------|
| Langage         | Java 17                 |
| Framework       | Spring Boot 3           |
| Messaging       | Apache Kafka            |
| DB              | PostgreSQL (server), Redis (chat) |
| Auth            | Keycloak + Spring OAuth2|
| WebSocket       | STOMP (Spring Messaging)|
| Conteneurs      | Docker + Docker Compose |
| Architecture    | Hexagonale modulaire    |

---

## Limites et points d’amélioration

### Inexistant

- Tests (unitaires, intégration, E2E) sur les microservices
- Monitoring, logs structurés, traçabilité
- Persistence durable des messages (uniquement sur Redis pour l'instant)
- Contrats OpenAPI / Swagger

### Imparfait

- Environnements et secrets codés en dur (`localhost`, `admin`, `postgres`)
- Front-end hors conteneur
- Auth Keycloak sans mapping de rôles/permissions
- Aucune distinction de rôles dans Spring Security
- Redis sans TTL
