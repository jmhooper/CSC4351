# CSC 4351 Projects

This repo holds work for the CSC 4351 class Compiler Construction.

## Getting Started

To get started, move to the directory you wish to work on, set the environment file, load the profile, and build the project

For example, to build project 1:

1. Move into the projects directory

```bash
cd prog1
```

2. Setup the environment file

```bash
cp example.env .env
vim .env
```

3. Load the profile

```bash
source .profile
```

4. Build and run the project

```bash
make
java Parse.Main test.tig
```