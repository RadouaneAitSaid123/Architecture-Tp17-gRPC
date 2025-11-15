# TP gRPC / Spring Boot – Gestion de comptes bancaires

## 1. Présentation du projet

Ce projet est un TP qui illustre l’utilisation de **gRPC** avec **Spring Boot** pour exposer un service de gestion de comptes bancaires.

Fonctionnalités principales :
- Consulter la liste de tous les comptes bancaires via un appel gRPC.
- Créer un nouveau compte bancaire via gRPC et le persister en base.
- Visualiser les données dans une base **H2** embarquée.

L’implémentation serveur gRPC se trouve notamment dans `src/main/java/com/aitsaid/tp17grpc/controllers/CompteServiceImpl.java` et s’appuie sur un service métier Spring `CompteService`.

---

## 2. Prérequis

- **Java JDK** (version compatible avec la propriété `<java.version>` définie dans `pom.xml`).
- **Maven** (ou utilisation des wrappers `mvnw` / `mvnw.cmd` présents à la racine du projet).
- Un IDE Java (IntelliJ IDEA, Eclipse, VS Code, …).
- Un client gRPC pour tester les services (par exemple : **BloomRPC**, Postman gRPC, Kreya, Evans, grpcurl, ou un client Java généré à partir du `.proto`).

---

## 3. Récupération et installation du projet

Cloner le dépôt (adapter l’URL avec ton compte GitHub) :

```bash
git clone https://github.com/<votre-compte-github>/<tp17-gRPC>.git
cd tp17-gRPC
```

Télécharger les dépendances et construire le projet :

```bash
mvn clean install
```

> Sous Windows, tu peux aussi utiliser le wrapper Maven :
>
> ```bash
> mvnw.cmd clean install
> ```

---

## 4. Lancement de l’application

L’application est une application **Spring Boot** qui démarre un serveur gRPC.

Depuis la racine du projet :

```bash
mvn spring-boot:run
```

ou avec le wrapper :

```bash
mvnw.cmd spring-boot:run
```

Tu peux également lancer la classe principale `Tp17GRpcApplication` depuis ton IDE.

Le port gRPC est configuré dans `src/main/resources/application.properties` (par exemple `grpc.server.port` si défini).

La base de données **H2** est généralement accessible via la console H2 si elle est activée, avec une URL du type :

- `http://localhost:8080/h2-console`  (cf. configuration dans `application.properties`).

---

## 5. Services gRPC exposés

Le fichier de définition gRPC se trouve dans :

- `src/main/proto/CompteService.proto`

Le service généré `CompteServiceGrpc` est implémenté par la classe :

- `src/main/java/com/aitsaid/tp17grpc/controllers/CompteServiceImpl.java`

### 5.1. Service `allComptes`

Signature côté Java (impl):

```java
@Override
public void allComptes(GetAllComptesRequest request,
                       StreamObserver<GetAllComptesResponse> responseObserver) {
    // ... implémentation ...
}
```

Rôle :
- Récupère la liste des comptes depuis la couche métier `compteService.findAllComptes()`.
- Convertit chaque entité JPA `Compte` en objet gRPC `Compte` (id, solde, date de création, type de compte).
- Envoie la réponse au client sous la forme d’un message `GetAllComptesResponse` contenant la liste des comptes.

Dans le code :
- `responseObserver.onNext(response)` envoie la réponse au client.
- `responseObserver.onCompleted()` termine correctement l’appel gRPC.

### 5.2. Service `saveCompte`

Signature côté Java (impl):

```java
@Override
public void saveCompte(SaveCompteRequest request,
                       StreamObserver<SaveCompteResponse> responseObserver) {
    // ... implémentation ...
}
```

Rôle :
- Récupère le `Compte` envoyé par le client dans `request.getCompte()`.
- Crée une entité JPA `com.aitsaid.tp17grpc.entities.Compte` et recopie les champs : `solde`, `dateCreation`, `type`…
- Persiste l’entité via `compteService.saveCompte(compte)`.
- Reconvertit l’entité sauvegardée en objet gRPC `Compte` (avec l’`id` généré) et la renvoie au client dans un `SaveCompteResponse`.

Comme pour `allComptes` :
- `responseObserver.onNext(...)` envoie la réponse au client.
- `responseObserver.onCompleted()` signe la fin de l’appel.

En cas d’erreur métier (ex : données invalides), on pourrait utiliser `responseObserver.onError(...)` pour renvoyer une erreur gRPC (voir section suivante).

---

## 6. Structure du projet

Structure simplifiée du projet :

- `pom.xml` : configuration Maven et dépendances (Spring Boot, gRPC, protobuf, etc.).
- `src/main/java/com/aitsaid/tp17grpc/`
  - `Tp17GRpcApplication` : classe principale Spring Boot.
  - `controllers/CompteServiceImpl.java` : implémentation du service gRPC.
  - `services/` : services métier, dont `CompteService`.
  - `entities/` : entités JPA (`Compte`, etc.).
  - `repositories/` : interfaces Spring Data JPA.
  - `stubs/` : classes gRPC générées à partir du `.proto` (par Maven/protoc).
- `src/main/proto/CompteService.proto` : définition des messages et services gRPC.
- `src/main/resources/`
  - `application.properties` : configuration Spring Boot (ports, H2, etc.).
  - `templates/`, `static/` : éventuels fichiers web.

Les classes générées à partir des fichiers `.proto` se trouvent dans :

- `target/generated-sources/protobuf/`

---

## 7. Captures d’écran

### 7.1. Tests gRPC avec BloomRPC

Les appels gRPC ont été testés avec **BloomRPC**, en utilisant le fichier `CompteService.proto`.

- <img width="1907" height="730" alt="add-compte" src="https://github.com/user-attachments/assets/701be43b-ec6b-4865-b338-fd6a99c6b1e6" />
  Test de la méthode `saveCompte` dans BloomRPC : envoi d’un nouveau compte et visualisation de la réponse (compte créé avec un `id` généré).

- <img width="1906" height="669" alt="all-comptes" src="https://github.com/user-attachments/assets/30ed3ed2-1894-471a-bea0-5b1b26af5870" />
  Test de la méthode `allComptes` dans BloomRPC : récupération et affichage de la liste de tous les comptes enregistrés.

- <img width="1907" height="642" alt="compte-by-id" src="https://github.com/user-attachments/assets/fc5dbf79-3819-4ec2-b2b7-4b1b21f782af" />
  Test d’un appel gRPC qui récupère un compte par son identifiant (si le RPC `getCompteById` est implémenté).

### 7.2. Base de données H2

- <img width="1004" height="506" alt="h2-db" src="https://github.com/user-attachments/assets/e4dd3283-f6af-419a-bad7-2525f91f5f6d" />
  Visualisation des comptes dans la console H2 après les appels gRPC (vérification que les données ont bien été persistées).

### 7.3. Total des comptes

-  <img width="1914" height="656" alt="total-compte" src="https://github.com/user-attachments/assets/cb61a979-1f7f-44c4-934a-8c496cd78668" />
  Exemple d’affichage d’un calcul ou d’une agrégation (par exemple le nombre total de comptes) si une méthode gRPC dédiée existe.

---
