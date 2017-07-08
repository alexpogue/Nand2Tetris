//
// Created by Alex Pogue on 9/17/16.
//

#include "tree.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

struct tree {
    char *data;
    struct tree **children;
    size_t numChildren;
    size_t childrenCapacity;
};

#define TREE_DEFAULT_CHILDREN_CAPACITY 4

tree_t *tree_deepCopy(tree_t *tree);
tree_t *tree_grow(tree_t *tree, size_t size);
void tree_printHelper(tree_t *tree, int indentLevel);

tree_t *tree_init(char *data) {
    tree_t *tree = malloc(sizeof *tree);
    if (!tree) {
        return NULL;
    }
    tree->children = malloc(TREE_DEFAULT_CHILDREN_CAPACITY * (sizeof *(tree->children)));
    if (!tree->children) {
        free(tree);
        return NULL;
    }
    tree->data = strdup(data);
    if (!tree->data) {
        free(tree);
        free(tree->children);
        return NULL;
    }
    tree->childrenCapacity = TREE_DEFAULT_CHILDREN_CAPACITY;
    tree->numChildren = 0;
    return tree;
}

char *tree_getData(tree_t *tree) {
    return tree->data;
}

tree_t *tree_setData(tree_t *tree, char *data) {
    if (tree->data) {
        free(tree->data);
    }
    tree->data = strdup(data);
    if (!tree->data) {
        return NULL;
    }
    return tree;
}

tree_t *tree_grow(tree_t *tree, size_t size) {
    if (!tree || size == 0) {
        return NULL;
    }
    tree_t **newChildren = realloc(tree->children, (sizeof *newChildren) * size);
    if (!newChildren) {
        return NULL;
    }
    tree->children = newChildren;
    tree->childrenCapacity = size;
    return tree;
}

tree_t *tree_addChild(tree_t *tree, tree_t *child) {
    if (!tree) {
        return NULL;
    }
    if (!child) {
        // quietly ignore attempt to add null child
        return tree;
    }
    if (tree->numChildren >= tree->childrenCapacity) {
        tree_t* newTree = tree_grow(tree, tree->childrenCapacity * 2);
        if (!newTree) {
            return NULL;
        }
        tree = newTree;
    }
    tree_t* childCopy = tree_deepCopy(child);
    if (!childCopy) {
        return NULL;
    }
    tree->children[tree->numChildren] = childCopy;
    ++(tree->numChildren);
    return tree;
}

tree_t *tree_getChild(tree_t *tree, size_t n) {
    if (!tree || tree->numChildren <= n) {
        return NULL;
    }
    return tree->children[n];
}

size_t tree_getNumChildren(tree_t *tree) {
    return tree->numChildren;
}

tree_t *tree_deepCopy(tree_t *tree) {
    tree_t *copy = tree_init(tree->data);
    if (!copy) {
        return NULL;
    }
    copy->numChildren = tree->numChildren;
    copy->childrenCapacity = tree->childrenCapacity;
    tree_t *newCopy = tree_grow(copy, tree->childrenCapacity);
    if (!newCopy) {
        free(copy);
        return NULL;
    }
    copy = newCopy;
    for (int i = 0; i < tree->numChildren; i++) {
        copy->children[i] = tree_deepCopy(tree->children[i]);
        if (!copy->children[i]) {
            for (int j = 0; j < i; j++) {
                free(copy->children[j]);
            }
            free(copy);
            return NULL;
        }
    }
    return copy;
}

void tree_free(tree_t *tree) {
    if (!tree) {
        return;
    }
    for (int i = 0; i < tree->numChildren; i++) {
        tree_free(tree->children[i]);
        tree->children[i] = NULL;
    }
    if (tree->children) {
        free(tree->children);
        tree->children = NULL;
    }
    if (tree->data) {
        free(tree->data);
        tree->data = NULL;
    }
    free(tree);
}

void tree_print(tree_t *tree) {
    if (!tree) {
        return;
    }
    tree_printHelper(tree, 0);
}

void tree_printHelper(tree_t *tree, int indentLevel) {
    int numSpacesPerLevel = 3;
    char *marker = "'- ";
    int numSpaces = (indentLevel - 1) * numSpacesPerLevel;
    for (int i = 0; i < numSpaces; i++) {
        printf(" ");
    }
    if (indentLevel > 0) {
        printf("%s", marker);
    }
    printf("%s\n", tree->data);
    for (int i = 0; i < tree->numChildren; i++) {
        tree_printHelper(tree->children[i], indentLevel + 1);
    }
}
