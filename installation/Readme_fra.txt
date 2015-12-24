MyJaPhoO

My Java Photo Organizer

MyJaPhoO est une application qui permet d'organiser de grandes collections de photos et vidéos.

Exigences

Ce logiciel nécessite l'installation sur votre système, de l'ensemble d'exécution « Java Runtime » version 1.6 ou plus récent.
Ce logiciel ne gère pas lui-même les fichiers vidéos, il utilise d'autres programmes :

- VLC pour générer les imagettes sur le système Windows et pour lire les vidéos. En option mplayer et kplayer peuvent être utilisés pour la lecture.
- ffmpegthumbnailer : pour générer les imagettes de vidéos sous linux.
Ces programmes ne sont pas fournis avec MyJaPhoO, vous devez les installer vous-même. S'il vous plaît, définissez le chemin d'accès à ces programmes dans le dialogue « Préférences » de MyJaPhoO.

Caractéristiques :

    Peut gérer de très grands ensembles de fichiers médias.
    Les données sont enregistrées dans une base de données relationnelle ; la base de données Derby est utilisée. Ainsi, il est possible d'y accéder en utilisant le langage de base de données SQL.
    Génération d'imagettes.
    Intégration d'un visionneur simple. Les films sont lus à l'aide d'un lecteur externe ; par exemple VLC.
    Les fichiers peuvent être étiquetés.
    Les étiquettes peuvent être structurés en arbre.
    Les étiquettes peuvent également être étiquetés par l'entremise de « méta étiquettes ». Ceci implique, qu'il est possible d'assigner une structure orthogonale.
    Commentaire, titre et cote peuvent être ajoutés.
    Fonctions filtres : une syntaxe spéciale appelée « Filtersyntax » permet de filtrer les attributs.
    Groupement par fonction : permet de produire des groupes de vues des médias par attributs ou regroupements définis par l'usager en utilisant une syntaxe particulière. Les regroupements multiples sont possibles.
    Recherche de doublons
        Les doublons sont identifiés à l'aide de somme de contrôle de type CRC32.
        Le regroupement par vue permet d'afficher les doublons.
        Destruction facile des doublons.
        Un filtre sur vue permet d'enlever les doublons de la vue seulement.
        Un filtre de doublon peut être créé par les regroupements.
    Une fonction de comparaison entre différentes base de données MyJaPhoO permet principalement d'y détecter les doublons.
    La disposition de l'interface utilisateur peut être ajustée et enregistrée dans des « Perspectives ».
    Création de signets qui sont constitués de filtre et regroupement.
    Fonction « défaire » et « refaire » pour les fonctions essentielles.
    Un journal des changements pour les fonctions essentielles.
    Une fonction d'historique pour retourner à des vues, filtres ou regroupements précédents.
    Fonction de copie des fichiers média.
    Fonction d'exportation et d'importation pour faciliter les interactions avec d'autres bases de données MyJaPhoO.
    L'utilisation du langage script Groovy est possible. Les macros peuvent être ajoutées comme commande de l'interface utilisateur dans les menus.
    Trois différentes vues d'imagettes :
        En général l'interface utilisateur est constituée de trois vues qui contiennent les fichiers médias pour la navigation et une vue détaillée qui montre les imagettes.
        Mode liste d'imagettes : affiche chaque imagette avec tous les attributs du fichier média.
        Mode imagettes : affiche les imagettes à l'aide d'une grille.
        Mode bande : affiche une bande pour chaque sous catégorie de l'arbre des médias sélectionné. Les bandes peuvent être ouvertes pour afficher toutes les imagettes de cette bande et toutes les imagettes d'une bande peuvent être sélectionnées par un double clique dans la première colonne.
    Vous pouvez ouvrir plusieurs interfaces utilisateur, chacune comportant ses propres filtres, regroupements et vues sur les données.
