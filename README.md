Nous avons choisi pour le contrôle d'accès une Basic authentication (utilisateur, mot_de_passe). Chaque utilisateur reçoit un rôle soit d'ADMIN soit de USER.

l'administrateur ne se crée qu'explicitement en BD depuis la console de H2 à l'adresse  http://localhost:9090/h2-console/ suivi des identifiants contenus dans le fichier application.properties.
        principe :              INSERT_INTO utilisateurs(`utilisateur_id`,`nom_utilisateur`,`email_utilisateur`) VALUE(1,"admin","admin@yahoo.fr")
        
        pour un user test :     INSERT_INTO utilisateurs(`utilisateur_id`,`nom_utilisateur`,`email_utilisateur`) VALUE(1,"user","user@gmail.com")
                                
Les deux roles aussi de même:   INSERT INTO role VALUE(1,"ADMIN_ROLE") et INSERT INTO role VALUE(2,"USER_ROLE")

la table `utilisateurs_roles` aussi de même selon ce principe:
                                INSERT INTO utlisateurs_roles(`utilisateur_id`,`role_id`) VALUE(1,1)
                                INSERT INTO utlisateurs_roles(`utilisateur_id`,`role_id`) VALUE(1,2)
                                INSERT INTO utlisateurs_roles(`utilisateur_id`,`role_id`) VALUE(2,2)

Il faut noter que nous avons choisi de faire réaliser automatiquement le choix du schéma de la base de données par hibernate.
Donc à l'exécution du code, les tables sont certe vides mais disponibles.
