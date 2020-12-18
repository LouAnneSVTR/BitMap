# Projet_BitMap_ASD3

Projet de compression d'image de taille 2^n * 2^n de coter. Le principe est d'enregistrer recusirvement tous les pixels dans un arbre quadratique, pour cela nous découpons l'image en 4 et pointons chaque branche un coin découpé, ainsi de suite. 

# DEMARRAGE

# mode non interactif
windows : Ouvrir ou placer vous avec une fenêtre  PowerShell dans le dossier qui contient Projet_BitMap_ASD3.jar. Lancer la commande "java -jar .\Projet_BitMap_ASD3.jar 'chemin ou nom de l'image**' 'valeur delta' 'valeur phi'".

linux : Ouvrir ou placer vous avec un terminal dans le dossier qui contient le Projet_BitMap_ASD3.jar. Lancer la commande "java -jar Projet_BitMap_ASD3.jar 'chemin ou nom de l'image**' 'valeur delta' 'valeur phi'".

**Pour charger une image, donnez le chemin ou mettez juste le nom (avec ou sans extension) si celle çi se situe dans le dossier "pngs" fournit avec.

# Mode interactif
windows : Ouvrir ou placer vous avec une fenêtre PowerShell dans le dossier qui contient Projet_BitMap_ASD3.jar. Lancer la commande "java -jar .\Projet_BitMap_ASD3.jar".

linux : Ouvrir ou placer vous avec un terminal dans le dossier qui contient le Projet_BitMap_ASD3.jar. Lancer la commande "java -jar Projet_BitMap_ASD3.jar".

# EXECUTION 

# Mode non interactif
Il suffit juste de mettre le nom ou le chemin de l'image, la valeur du delta et la valeur du phi en argument lors de l'exécution. Deux nouvelles images apparaîtrons alors dans "SavePNG" nomImage-delta"nombreDelta".png et nomImage-phi"nombrePhi".png qui sont les compression delta et phi de l'image et deux nouveaux fchier txt apparaîtrons dans "SaveTXT" nomImage-delta"nombreDelta".txt et nomImage-phi"nombrePhi".txt qui correspondent aux fichiers textes des arbres compressés.

Un rapport de poids s'affichera dans le terminal ou le PowerShell.

# mode intercatif 

Au début du programme, il sera demandé de charger une image avant d'accéder au menu. Pour charger une image, donnez le chemin ou mettez juste le nom (avec ou sans extension) si celle ci se situe dans le dossier "pngs" fournit avec.

Il vous sera possible ensuite de choisir de recharger une image (0), de faire une compression delta/phi(1/2) et afficher l'arbre en écriture (A). 

Une des options de compression permettra de sauvegarder votre image en .png (4) dans le dossier "SavePNG" fournit avec ou de sauvegarder dans un .txt (5) l'affichage écrit de votre arbre dans le dossier "SaveTXT" fournit avec le .jar.

Sauvegarder votre image en png permettra ensuite d'effectuer les mesures comparatives (6) de l'image d'origine avec l'image compresser.

# AUTEURS
Sofiane COUËDEL : étudiant en licence 3 Informatique Nantes.
Lou-Anne SAUVÊTRE : étudiante en licence 3 Informatique Nantes.
