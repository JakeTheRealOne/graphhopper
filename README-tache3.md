# Tâche 3 – Partie GitHub Actions (GA)

# Binôme
- Bilal Vandenberge  
- Ilyas Ally Musaphur 

# 1. But de notre modification

Pour la tâche 3, on a modifié le workflow GitHub Actions de notre fork de GraphHopper pour :

- lancer les tests automatiquement à chaque push ;
- faire échouer le build si le score de mutation est plus bas que la dernière valeur acceptée ;
- ajouter un peu d’humour quand le build échoue.

# 2. Fichiers utilisés

Nous avons travaillé dans notre fork :

- Repo : `https://github.com/JakeTheRealOne/graphhopper`

Fichiers liés à la partie Github Actions :

- `.github/workflows/build.yml`  
- `mutation_baseline.txt`

# 3. Comment fonctionne notre workflow

# 3.1 Job `build` (compilation + tests)

Dans `build.yml`, nous avons un job `build` avec une matrix de versions Java :


Code :
matrix:
  java-version: [24, 25-ea]


Pour chaque version de Java, le workflow :

1. récupère le code (actions/checkout@v4),
2. installe Java (actions/setup-java@v4),
3. utilise le cache Maven / Node,
4. lance les tests unitaires :


Code :
- name: Build and unit tests on Java ${{ matrix.java-version }}
  run: mvn -B clean test


Ce code permet de vérifier que GraphHopper compile et que les tests passent sur Java 24 et 25-ea.

# 3.2 Score de mutation

Ensuite, on s’occupe du score de mutation.
On ne le calcule que pour Java 24 pour éviter les doublons :


Code :
- name: Run mutation tests and compute score (Java 24 only)
  if: matrix.java-version == 24
  run: |
    # À remplacer plus tard par la vraie commande de mutation
    # Par exemple : mvn -B org.pitest:pitest-maven:mutationCoverage

    # Pour l’instant, on met un score fictif pour tester la logique
    echo "80.0" > mutation_score.txt


Pour l’instant, on met simplement `80.0` dans `mutation_score.txt`.
L’important pour la tâche 3 est la **logique de comparaison**, pas encore le calcul réel.

# 3.3 Fichier `mutation_baseline.txt`

On a ajouté à la racine du projet un fichier texte : mutation_baseline.txt

Ce fichier contient un seul nombre, par exemple : 80.0

Ce nombre est la baseline, à partir de ce score, on ne veut plus jamais descendre en dessous.

Si un jour on améliore le score de mutation, on pourra mettre une nouvelle valeur dans ce fichier et la commit.

# 3.4 Comparaison : nouveau score vs baseline

Le step suivant compare le score courant à la baseline :

Code :
- name: Check mutation score did not decrease (Java 24 only)
  if: matrix.java-version == 24
  run: |
    echo "=== Mutation score comparison ==="

    # Si le fichier baseline n’existe pas, on le crée à 0.0
    if [ ! -f mutation_baseline.txt ]; then
      echo "Aucun fichier baseline trouvé, utilisation de 0.0"
      echo "0.0" > mutation_baseline.txt
    fi

    NEW_SCORE=$(cat mutation_score.txt)
    BASELINE_SCORE=$(cat mutation_baseline.txt)

    echo "Baseline: $BASELINE_SCORE"
    echo "New:      $NEW_SCORE"

    # Comparaison avec awk (supporte les nombres décimaux)
    is_less=$(awk -v new="$NEW_SCORE" -v base="$BASELINE_SCORE" 'BEGIN { if (new < base) print "yes"; else print "no"; }')

    if [ "$is_less" = "yes" ]; then
      echo "❌ Mutation score decreased: $NEW_SCORE < $BASELINE_SCORE"
      exit 1        # => le job build échoue
    else:
      echo "✅ Mutation score OK: $NEW_SCORE >= $BASELINE_SCORE"
    fi


Si le nouveau score < baseline alors, on affiche un message d’erreur et on fait exit 1 et le job build échoue.
Sinon le job build continue normalement.

# 4. Humour : job rickroll

On a ajouté un deuxième job pour l’humour :

Code :
rickroll:
  needs: build
  runs-on: ubuntu-latest
  if: failure()
  steps:
    - name: Never gonna give you up 😈
      run: |
        echo "Les tests ou le score de mutation ont échoué..."
        echo "Never gonna give you up, never gonna let you down 🎵"
        echo "https://www.youtube.com/watch?v=dQw4w9WgXcQ"


- `needs: build` : ce job dépend du job `build`.
- `if: failure()` : il ne s’exécute que si `build` a échoué.
-  En cas d’échec, le CI affiche un message + un lien Rickroll.

Cela correspond au critère d'humour.


# 5. Comment on a testé notre workflow

Pour vérifier que tout fonctionne comme prévu, on a fait **deux tests**.

# 5.1 Test 1 – Succès (score OK)

1. On met dans mutation_baseline.txt : 0.0 puis 80.0.

2. Le step de mutation écrit 80.0 dans mutation_score.txt.

3. Dans les logs GitHub Actions, on voit :


Baseline: 0.0
New:      80.0
Mutation score OK: 80.0 >= 0.0


4. Le job build est vert (SUCCESS) et le job rickroll est skipped.

# 5.2 Test 2 – Échec (score plus bas que la baseline)

1. On augmente la baseline dans mutation_baseline.txt : 90.0

2. Le step de mutation produit toujours 80.0 dans mutation_score.txt.

3. Dans les logs, on voit :


Baseline: 90.0
New:      80.0
Mutation score decreased: 80.0 < 90.0


4. Le job build devient rouge (FAILED) et le job rickroll s’exécute et affiche le message + le lien pour la vidéo humour.

Ces deux scénarios montrent que :

- la GA réussit quand le score de mutation ne baisse pas ;
- la GA échoue quand le score baisse, comme demandé dans les critères ;
- l’élément d’humour est bien déclenché en cas d’erreur.



