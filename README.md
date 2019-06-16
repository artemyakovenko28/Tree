# Tree
Tree implementation on Java
This repository contains tree implementation on Java as generic container for items of arbitrary structure.

## Features
1. store items 
2. build/modify/access tree structure 
    -	add/remove children of a particular parent
    -	get subtrees starting from the particular parent
3. search for items based on filters
4. iterating over the tree according to different iteration strategies. See more about iteration strategies 
<a href="https://en.wikipedia.org/wiki/Tree_traversal">here</a>

## Building info
Open bash shell <br/>
To build an executable jar, run `gradlew clean build` from the project root<br/>
`cd build/libs`<br/>
`java -jar $(ls)`

## Testing info
Test info
Implementation is covered by unit tests for structural modifications 
and `Tree` interface operations
Used groovy SPOCK framework for this purpose
To ensure everything works correctly run `gradlew clean test`

