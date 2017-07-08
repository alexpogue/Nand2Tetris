//
// Created by Alex Pogue on 9/17/16.
//

#ifndef TREE_TREE_H
#define TREE_TREE_H

#include <stddef.h>

typedef struct tree tree_t;

tree_t *tree_init(char *data);
char *tree_getData(tree_t *tree);
tree_t *tree_setData(tree_t *tree, char *data);
tree_t *tree_addChild(tree_t *tree, tree_t *child);
tree_t *tree_getChild(tree_t *tree, size_t n);
size_t tree_getNumChildren(tree_t *tree);
void tree_free(tree_t *tree);
void tree_print(tree_t *tree);

#endif //TREE_TREE_H
