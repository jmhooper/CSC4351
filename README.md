# CSC 4351 Projects

This repo holds work for the CSC 4351 class Compiler Construction.

### Setup Script

To get started pull the repo and run the setup script.

The setup script will guide you through the process of creating an environment file, and downloading the file from the classes server.

```bash
clone git@github.com:jmhooper/CSC4351.git
./setup
```

After you run the setup script, you should have all the files you need to build the projects.

### Aliases

Some aliases for working with the projects are provided in the file named ".aliases". You can run `source .aliases` to load these aliases.

If a command is not added to your .bashrc to load these aliases when your terminal is loaded,
then you will have to re-load the aliases everytime you open the project in a new terminal window.

### Profiles

Inside each project directory there is a file called ".profile".
These are some variables that make the project work.

Everytime you switch to a project, you will need to load that projects profile.

```bash
cd prog1/
source .profile
```

If you loaded the aliases file, then you can use the aliases to switch between project without having to set the profile.
The alias `_prog1` can be used to load Project 1, `_prog2` for Project 2, and so on.

```bash
_prog1
```

### Build and run the project

After everything is configured, you should be able to use a projects makefile to build the project.

```bash
_prog1
make
java Parse.Main test.tig
```