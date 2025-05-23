# Rapport de développement

## Architecture du projet 

Le projet est coder en Kotlin avec Jetpack Compose. Il est divisé en plusieurs Activity où chaque page de l'application est une Activity. 

La base de donnée est Room. On y trouve les tables : 

- Alerts
- HTTPTest
- TCPTest
- UDPTest 
- ICMPTest
- Log
- TapTap
- Settings

Mais aussi des composant afin de nous aider dans le code : 

- Background
- CheckBox
- DropDownMenu
- InputTextField
- InputTextFieldNumber
- NevigationButton

On y trouve également une page Protocol_Chooser, permettant de choisir les tests qu'on veux accéder. Afin de créer un nouveau protocol (si l'envie nous prend), il suffit de créer une activity avec les algorithme du protocole, puis de rajouter un bouton (NavigationButton) dans l'activity Protocol_Chooser afin d'accéder à la page de notre protocol fraichement créer.

Le dossier Entity liste les entity utilisé dans la base de donnée.

Le dossier DAO liste les DAO utilisé dans la base de donnée. Les DAO servent à faire des requêtes dans la base de donnée à une certaine table.

Il y a aussi la classe TestService qui permet de démarrer où arrêter les tests automatiques et le service avant-plan

La classe WatchoidApp permet de créer le notification channel et la classe AlertNotificationService permet d'afficher les notifications

L'application possède une Activity pour chaque protocol:

- HTTPActivity
- TCPActivity
- UDPActivityUser
- ICMPActivityUser

Les mini-jeux:

TapTap : peut être démarré en cliquant sur le texte de l'en-tête plusieurs fois

TicTacToe : peut être démarré en maintenant le bouton "Stop tests" appuyé

## Défis rencontré

Mathéo : 

J'ai eu beaucoup de problème sur ICMP car je comprennais pas pourquoi la requête ne fonctionnait pas sur un serveur distant autre que localhost. J'ai essayé plusieurs méthode et plusieurs algorithme. Cela ne fonctionnait pas car j'utilisais l'application via l'émulateur et l'émulateur n'arrive pas à ping un serveur distant (www.google.com). J'ai testé sur un vrai téléphone physique et cela à fonctionner.

A part ça, je n'ai eu de gros problème. Les activity à faire, leur style et les algorithmes à faire pour les protocoles n'étaient pas compliqué, juste ce fut long à faire.

Ilyass :

J'ai eu des problème en essayant d'automatiser les tests, en faisant des recherches j'ai trouvé que je peux créer une classe qui hérite de Service() qui m'aidera à faire cette tâche

J'ai aussi rencontré une difficulté à afficher les notification mais je ne savais pas que j'avais besoin d'une classe héritant de Application() qui créera les notifications channels.

Pour les tests TCP j'avais une difficulté à trouver comment gérer plusieurs cas pour laisser la liberté à l'utilisateur de configurer son test


## Répartition des tâches

On s'est réparti les tâches assez simplement et rapidement. On s'est d'abord concentré sur le codage des algorithme pour les protocoles. Donc Ilyass s'est concentré sur HTTP et TCP et Mathéo s'est concentré sur ICMP et UDP. 

Après que les protocoles ont les assemblés dans un interface graphique. Mathéo à fait la partie graphique pour les protocoles qu'il avait coder comme pour Ilyass. 

Après Mathéo à fait le reste de la partie graphique, page d'accueil, page pour choisir les protocoles, paramètres et liste des alertes. Il aussi mis en place la bdd et le fait que les tests s'ajoute dans la base de donnée.

Dans le même temps Ilyass s'est concentré sur la fonctionnalité de l'automatisation des tests tournant en tâche de fond. En faisant ça, il a dû changer quelques table voire en rajouter dans la base de donnée. Ainsi que des modifier l'interface graphique de la page d'accueil.

Il a aussi fait la partie de vérification de connexion et l'affichage des alertes.