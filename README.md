# Infinite Recharge 2020

This is the official code of team Spikes#2212 for the 2020 FRC season.

## Code Conventions

#### General

1. This project is developed according to our java code style convention,
   described [here](https://docs.google.com/document/d/1rGkyMf1XVvJ3K8mu5f1Gea2YFeEx2MpBVQo05D9ZVtY/edit?usp=sharing).
2. Each branch is named in `lower-case`, according to the convention `author-feature`.
3. Commit messages are written in `lower case`.

#### Development

#### Code Conventions

All code in this project should be written according to the following convention, code would not be merged in case it
doesn't.

1. All classes should be written in the following order
    1. Constants Values
    2. Class Members
    3. Singleton Initialization
    4. Constructor
    5. Methods
2. All `Namespace` instances should be named in `lower case`.

#### Component Naming Conventions

1. All CANBus components should be named in `lower-case` according to the following convention
    - `subsystem-side-controller \ index`
2. All ports in RobotMap should be named in `ALL_CAPS` according to the following convention `SUBSYSTEM_COMPONENT_INDEX`

##### Feature Branches

This code is developed using the feature branches workflow.

Each feature is developed inside its own branch, which is merged into dev after CR and testing.

##### Testing

Each feature branch should be forked by a `feature-branch-name-testing` branch, in which all necessary testing code is
added. <br>
all the code fixes should be committed into this branch.

After the code passes testing all the commits from the `-testing` are merged with the feature branch.

##### DEV Branch

All feature branches that passed testing successfully are later merged into the `dev` branch, which is in turn merged
into `master` after passing complete testing and integration.