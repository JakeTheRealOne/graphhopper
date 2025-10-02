## Tâche 2 : Tests 

## Binôme
- Bilal Vandenberge  
- Ilyas Ally Musaphur  


## Classes sélectionnées
Dans le cadre de cette tâche, nous avons choisi d’améliorer la couverture de tests sur les classes suivantes :  
- `GHUtility` (classe utilitaire contenant de nombreuses méthodes statiques liées à la manipulation de graphes)  
- `StopWatch` (classe permettant de mesurer le temps d’exécution d’un code de manière simple)  
- `AngleCalc` (classe permettant de convertir des azimuts en points cardinaux ou en angles relatifs aux axes)  

Ces classes avaient une couverture partielle, avec des méthodes non testées ou mal couvertes.  
Nos nouveaux tests ont permis de renforcer la robustesse de ces classes et d’améliorer le **score de mutation**.


## Documentation des tests

Chaque test est documenté avec :  
- **Nom du test**  
- **Intention** : comportement visé  
- **Données de test** : valeurs utilisées  
- **Oracle** : comportement attendu  
- **Justification** : pourquoi ce test est pertinent  


### GHUtilityTest

## 1. `testPathsEqualExceptOneEdge_samePathsThrows`
- **Intention** : Vérifier que la méthode privée `pathsEqualExceptOneEdge` lève une exception si deux chemins identiques sont comparés.  
- **Données** : deux listes de nœuds `{1, 2, 3}`.  
- **Oracle** : `IllegalArgumentException`.  
- **Justification** : cette situation est explicitement interdite par le code source ; la tester garantit qu’un bug ne masquera pas ce comportement.  

## 2. `testGetAdj`
- **Intention** : Vérifier que `getAdjNode` renvoie correctement le nœud adjacent d’une arête.  
- **Données** : graphe de 4 nœuds avec arête `(0, 3)`.  
- **Oracle** :  
  - `getAdjNode(graph, 0, 0)` → `0`.  
  - `getAdjNode(graph, 0, 3)` → `3`.  
- **Justification** : garantit le bon fonctionnement de la méthode pour un graphe simple.  

## 3. `testGetEdge`
- **Intention** : Vérifier que `getEdge` récupère correctement une arête et ses informations associées.  
- **Données** : graphe avec deux arêtes : `(0, 3)` de distance `0.67`, et `(41, 42)`.  
- **Oracle** :  
  - `getEdge(graph, 0, 3).getBaseNode()` → `0`.  
  - `getEdge(graph, 42, 41).getBaseNode()` → `42`.  
  - `getEdge(graph, 0, 3).getDistance()` → `0.67`.  
- **Justification** : valide la récupération correcte des arêtes et distances.  


## StopWatchTest

## 4. `testToStringWithName`
- **Intention** : Vérifier que `toString()` inclut bien le nom donné et le temps écoulé.  
- **Données** : `StopWatch("customName")`.  
- **Oracle** : chaîne contient `"customName"` et `"time:"`.  
- **Justification** : assure la lisibilité des mesures.  

## 5. `testGetTimeStringZero`
- **Intention** : Vérifier la valeur par défaut du chronomètre non démarré.  
- **Données** : `new StopWatch()` sans `start()`.  
- **Oracle** : `"0ns"`.  
- **Justification** : confirme la robustesse dans un scénario de non-utilisation.  

## 6. `testGetMillisDoubleAfterSleep`
- **Intention** : Vérifier que la mesure du temps est correcte après une pause.  
- **Données** : `StopWatch.started()`, `Thread.sleep(10)`, puis `stop()`.  
- **Oracle** : valeur ≥ `9 ms`.  
- **Justification** : simule une utilisation réelle pour mesurer une durée d’exécution.  


## AngleCalcTest

## 7. `testAzimuth2compassPoint`
- **Intention** : Vérifier que `azimuth2compassPoint` convertit correctement un azimut en points cardinaux.  
- **Données** : `22.0°, 67.0°, 112.0°, 157.0°, 202.0°, 247.0°, 292.0°, 337.0°`.  
- **Oracle** : `"N"`, `"NE"`, `"E"`, `"SE"`, `"S"`, `"SW"`, `"W"`, `"NW"`.  
- **Justification** : couvre les 8 points cardinaux principaux.  

## 8. `testConvertAzimuth2xaxisAngle`
- **Intention** : Vérifier que `convertAzimuth2xaxisAngle` convertit correctement un azimut en angle relatif à l’axe X et gère les valeurs invalides.  
- **Données** :  
  - `42.0° → 0.0775804 rad`.  
  - `90.0° → 1.54338 rad`.  
  - `269.0° → -3.1241 rad`.  
  - `361° → IllegalArgumentException`.  
  - `-1° → IllegalArgumentException`.  
- **Oracle** : valeurs numériques précises (±0.0001), exceptions pour valeurs invalides.  
- **Justification** : couvre les cas normaux et limites.  


## Résultats d’exécution
- Les **7 nouveaux tests** s’exécutent avec succès (`mvn test`).   
- Tous les tests passent également dans l’intégration continue (GitHub Actions).  


En résumé :  
- **7 nouveaux tests ajoutés**  
- **Tous les tests passent localement et sur GitHub Actions**  

