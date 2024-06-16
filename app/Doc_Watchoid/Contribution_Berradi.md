# Contributions Ilyass BERRADI

## Tests réseau

### Tests TCP

Pour les tests j'étais responsable des tests TCP et HTTP. Pour le test TCP, au début il configure le buffer d'envoi, j'ai ajouté
un champ où l'utilisateur peut saisir sa requête, un menu deroulant où il choisit le type de la requête (qui est soit un Int, un Double, un Long ou un String), un checkbox qu'il coche s'il veut mettre la taille de la chaîne encodée cette dernière, un checkbox qu'il coche s'il
veut fermer l'input à la fin après l'envoi et l'encodage de cette requête. L'utilisateur peut du coup ajouter plusieurs requête au buffer
de l'envoi. À chaque fois qu'il veut ajouter des données, il va appuyer sur le bouton "Ajouter au buffer d'envoi". Après vient l'étape
de configuration du buffer de réponse. D'abord à l'aide du menu déroulant, l'utilisateur va choisir le type de réponse qu'il attend, 
ce qui donnera une idée sur la taille du buffer de réponse. Ensuite, si le type est un String, il y a un autre menu déroulant pour choisir l'encodage de
la réponse, pour savoir comment l'application doit la décoder. Puis, j'ai ajouté un champ Size où nous saisissons la taille du buffer de réponse,
si la taille n'est pas défini, l'application va lire le int du buffer de réponse qui sera la taille de la chaîne qui vient
après. Quand nous finisson cette configuration, nous appuyons sur le bouton "Ajouter au buffer de réponse". Ensuite, j'ai mis un champ
où nous saisissons la réponse attendu, qui sera comparé avec la réponse reçu. Après, il y a de champs de saisie,
un pour l'adresse du serveur ou du destinataire et son port. Enfin, quand toutes les informations nécessaires sont précisé, nous pouvons envoyer
la requête à l'aide du bouton "Envoyer". Ce bouton va executé ce manuellement le test et l'ajouter à la base de données avec son alerte
correspondante. 

S'il y a une IOException, je la catch et j'affiche le résultat false, si l'utilisateur saisit une addresse non existante je catch UnresolvedAddressException
et j'affiche un toast qui informe l'utilisateur que l'adresse est non connu

### Tests HTTP

Pour le test HTTP j'ai ajouté un champ où nous saisissons l'URL et à côté se trouve un menu déroulant permettant de choisir si nous
attendons un Text, du code Html ou un objet JSON. Dans le cas où l'utilisateur choisit Text un nouveau champs va s'ajouter à l'interface
qui s'appelle pattern, l'utilisateur saisira le regex qu'il attend trouver dans la réponse. Au cas où il choisit Html, il y a deux champs
qui s'affiche, "Tag" et "Pattern" pour Tag l'utilisateur devra saisir le XPath qui représente un certain chemin dans le texte Html,
Par exemple si nous saisissons //div/p, cela veut dire que nous cherchons tous les éléments p présents dans un div. Ensuite, dans le champ
Pattern l'utilisateur saisit la valeur qui se trouve dans ce chemin. Enfin, si l'utilisateur choisit JSON, un champs "Path" s'affichera
où l'utilisateur met le chemin sous cette forme par exemple :

```JSONPath
$.racine.clé1.clé2
```

qui veut dire le chemin commençant de la racine "racine" jusqu'à la clé "clé2". Après il y a un deuxième champ qui s'affiche avec un
menu déroulant où l'utilisateur précise la valeur qu'il cherche dans ce chemin et son type qui est soit Int, Long, Double ou Boolean.

Après avoir saisi les champs nécessaires, l'utilisateur peut cliquer sur le bouton "envoyer" ce qui déclenchera le test et l'ajoutera
à la base de données, la réponse  s'affichera ensuite sur un Box scrollable et en dessous
le booléen qui définit si le test est bien passé où non.


## Création de classe service pour l'automatisation des tests

Pour l'automatisation, j'ai d'abord crée une classe TestService qui hérite de la classe Service(), ensuite j'ai implémenté la fonction
onStartCommand qui soit démarre les tests automatiques où les arrête. Pour le démarrage, elle utilise la fonction start() qui crée et execute un 
nouveau objet Notification en avant-plan qui informera l'utilisateur de l'execution des tests et ensuite déclenche les test automatique.
Après, j'ai implémenté la fonction stop() qui arrête les tests automatique. Le onStartCommand execute une action en fonction de l'action
de l'intent passée en paramètre. Il fallait aussi que j'ajoute ce service au AndroidManifest.xml. Enfin, j'ai ajouté les boutons "Start tests" et
"Stop tests" qui démarre la classe service TestService

## Création des notifications

Pour les notifications, j'ai d'abord créé une classe WatchoidApp qui hérite de la classe Application(), après avoir ajouter cette classe
Application au AndroidManifest.xml, j'ai ensuite créé une notification channel pour le service avant-plan, les alertes et les notifications
de connectivités réseau. Ensuite, j'ai implémenté une classe AlertNotificationService qui implémente les fonctions d'affichage des notifications.
Enfin, j'ai ajouté une fonction ActivityCompat.requestPermissions dans MainActivity qui demande la permission d'afficher des notifications
à l'utilisateur.

## Vérification de la connectivité réseau

Pour cela j'ai implémenté une fonction isConnectedToNetwork qui vérifie si l'utilisateur est connecté au wifi ou cellulaire

## Automatisation des tests TCP et HTTP

Pour automatiser le test TCP j'ai utiliser une fonction suspend "automaticTCPTest" qui déclenche les tests automatiques. Au début, elle récupère tous les TCP tests et leur periodicité de la base de données.
Ensuite, elle execute une boucle infini qui à chaque boucle vérifie l'état de la connectivité réseau, si l'utilisateur n'est pas connecté, l'application affiche une notification
informant l'utilisateur qu'il n'y a pas de connexion réseau et arrête les tests. S'il y a la connectivité, une boucle qui parcourt test par test est executé. Pour chaque execution de test, 
un log représentant le résultat est ajouté à la base de donnée. Ensuite, nous analysons le résultat du test, si le test retourne une erreur, nous incrémentant le nombre d'erreur
de l'alerte correspondante au test, si le nombre d'erreurs arrive à la limite qui est configuré au Settings de l'application, une notification alertant l'utilisateur est affiché. Le compteur
d'erreur pour ce test est remis à 0 et si le test passe une nouvelle fois, l'utilisateur est notifié par le succés du test grâce à l'affichage d'une notification. Pour les tests
HTTP, ils sont executés de la même manière


## Création de la table Log

Pour l'affichage des log, j'ai créé une table log qui contient la date du test, l'id du test, son résultat et son protocole

## Définition des champs de la table tcp_tests et http_tests

Pour les tests TCP et HTTP j'ai redéfini les attributs nécessaire qui sont les valeurs dont nous aurons besoin pour les tests automatiques

## Mini-jeu TicTacToe

J'ai implémenté le jeu TicTacToe qui est un jeu à deux joueurs, le jeu peut être démarré en maintenant le bouton "Stop tests" appuyé