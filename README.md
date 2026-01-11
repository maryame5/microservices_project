# Architecture Microservices - Bibliothèque

## 📋 Vue d'ensemble du projet

Nom / Prénom : EL KHALFI MARYAME


Cette application est une architecture microservices complète pour la gestion d'une bibliothèque avec:

### 🏗️ Services
1. **Eureka Server** - Service de découverte (port 8761)
2. **API Gateway** - Point d'entrée unique (port 9999)
3. **User Service** - Gestion des utilisateurs (port 8082)
4. **Book Service** - Gestion des livres (port 8081)
5. **Emprunter Service** - Gestion des emprunts (port 8085) - Producteur Kafka
6. **Notification Service** - Service de notifications asynchrones (port 8086) - Consommateur Kafka

### 🗄️ Infrastructure
- **MySQL 8.4** - Base de données (port 3306) - _Database per Service_ via 3 schémas :
  - `db_user` - Base de données User Service
  - `db_book` - Base de données Book Service
  - `db_emprunter` - Base de données Emprunter Service
- **Kafka** - Message broker pour la communication asynchrone (port 9092)
- **Zookeeper** - Coordination Kafka (port 2181)

### 🔄 Flux de communication

#### Communication Synchrone (REST)
```
Client → API Gateway → Services (User, Book, Emprunter)
```

#### Communication Asynchrone (Kafka)
```
Emprunter Service (Producteur)
         ↓
    Topic: emprunt-created
         ↓
Notification Service (Consommateur)
```

---

## 🚀 Démarrage de l'application

### Prérequis
- Docker et Docker Compose installés
- Java 23 (pour développement local hors conteneur)
- Maven 3.8+

### Démarrage avec Docker Compose

```bash
# 1. Aller dans le répertoire du projet
cd d:\TP1_MOCROSERVICE\microservicesapp

# 2. Démarrer (build + run)
docker-compose up --build

# Relancer en arrière-plan
docker-compose up -d
```

### Accès aux services

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Server | http://localhost:8761 | Découverte des services |
| API Gateway | http://localhost:9999 | Point d'entrée |
| User Service | http://localhost:8082 | Gestion utilisateurs |
| Book Service | http://localhost:8081 | Gestion livres |
| Emprunter Service | http://localhost:8085 | Gestion emprunts |
| Notification Service | http://localhost:8086 | Notifications |

---

### Configuration Base de Données

MySQL (unique instance, 3 bases) :
```
Host: localhost:3307
User: crm_user
Password: crm_password
Databases: db_user, db_book, db_emprunter
```

---

## 📨 Architecture Kafka

### Topic: `emprunt-created`

**Producteur:**
- Service: `Emprunter Service`
- Événement: Lors de la création d'un nouvel emprunt
- Payload:
```json
{
  "empruntId": 1,
  "userId": 1,
  "bookId": 5,
  "createdAt": "2024-01-11T10:30:00",
  "message": "Nouvel emprunt créé: Utilisateur 1 a emprunté le livre 5"
}
```

**Consommateur:**
- Service: `Notification Service`
- Groupe: `notification-group`
- Action: Logging des notifications et possibilité d'étendre (email, SMS, etc.)

---

## 🔧 Commandes Docker Compose utiles

```bash
# Afficher les logs en temps réel
docker-compose logs -f

# Afficher les logs d'un service spécifique
docker-compose logs -f emprunter-service

# Arrêter tous les services
docker-compose down

# Arrêter et supprimer les volumes
docker-compose down -v

# Redémarrer un service spécifique
docker-compose restart user-service

# Voir le statut des services
docker-compose ps
```

---

## 📝 Points d'API Exemples

### User Service (8082)
```
GET /users - Lister les utilisateurs
POST /users - Créer un utilisateur
GET /users/{id} - Obtenir un utilisateur
```

### Book Service (8081)
```
GET /books - Lister les livres
POST /books - Créer un livre
GET /books/{id} - Obtenir un livre
```

### Emprunter Service (8085)
```
POST /emprunts - Créer un nouvel emprunt
GET /emprunts - Lister les emprunts
GET /emprunts/{id} - Détails d'un emprunt
```

---

## 🎯 Flux d'une création d'emprunt

1. **Requête reçue** - Client envoie `POST /emprunts` via la Gateway
2. **Validation** - Emprunter Service valide user et book existence
3. **Persistance** - Emprunt sauvegardé dans `db_emprunter`
4. **Event Kafka** - Événement `emprunt-created` publié sur le topic
5. **Notification** - Notification Service consomme l'événement
6. **Logging** - Notification loggée sur la console

---

## 🔍 Monitoring et Debugging

### Voir les événements Kafka
```bash
# Accéder au conteneur Kafka
docker-compose exec kafka bash

# Lister les topics
kafka-topics --list --bootstrap-server localhost:9092

# Consommer les messages (dans un terminal)
kafka-console-consumer --bootstrap-server localhost:9092 --topic emprunt-created --from-beginning
```

### Vérifier MySQL
```bash
# Accéder au conteneur MySQL
docker-compose exec mysql-db mysql -u crm_user -pcrm_password

# Lister les bases
SHOW DATABASES;
```

---

## ⚙️ Structure des dossiers

```
microservicesapp/
├── docker-compose.yaml      # Orchestration tous services
├── init-db.sql              # Initialisation PostgreSQL
├── eurika/                  # Service Eureka
├── gateway/                 # API Gateway
├── user/                    # User Service
├── book/                    # Book Service
├── emprunter/               # Emprunter Service + Producteur Kafka
│   ├── src/main/java/com/org/emprunt/
│   │   ├── event/           # Événements Kafka
│   │   ├── kafka/           # Producteur Kafka
│   │   └── ...
├── notification/            # Notification Service (nouveau) + Consommateur Kafka
│   ├── src/main/java/com/org/notification/
│   │   ├── event/           # Événements Kafka
│   │   ├── kafka/           # Consommateur Kafka
│   │   └── ...
└── README.md
```

---

## 📚 Technologies utilisées

- **Spring Boot 3.4.1** - Framework application
- **Spring Cloud 2024.0.0** - Microservices
- **Spring Data JPA** - Persistence
- **Spring Kafka** - Message broker
- **PostgreSQL 15** - Base de données
- **Apache Kafka 7.5.0** - Event streaming
- **Eureka** - Service discovery
- **Spring Cloud Gateway** - API Gateway
- **OpenFeign** - Communication inter-services
- **Lombok** - Réduction du boilerplate

---

## 🐛 Troubleshooting

### Kafka ne démarre pas
```bash
# Vérifier que Zookeeper est prêt
docker-compose logs zookeeper

# Redémarrer Kafka
docker-compose restart kafka
```

### Les services ne découvrent pas Eureka
```bash
# Vérifier qu'Eureka est prêt
docker-compose logs eureka-server

# Vérifier la configuration réseau
docker network inspect microservicesapp_biblio-network
```

### PostgreSQL connection refused
```bash
# S'assurer que le conteneur PostgreSQL est démarré
docker-compose ps | grep postgres

# Vérifier les logs PostgreSQL
docker-compose logs postgres-db
```

---

## 📞 Support et évolution

### Prochaines étapes possibles:
- [ ] Ajouter authentification/autorisation (Spring Security)
- [ ] Implémenter des transactions distribuées (Saga pattern)
- [ ] Ajouter des métriques (Micrometer/Prometheus)
- [ ] Configurer log centralisé (ELK Stack)
- [ ] Ajouter des tests d'intégration
- [ ] Déploiement sur Kubernetes

---

**Dernière mise à jour:** 11 Janvier 2026
