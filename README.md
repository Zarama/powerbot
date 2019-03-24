# The Open Script Repository
The Open Script Repository allows non-ranked script writers to publish their scripts to SDN.
<p>
If you have a script that you would like to publish, read on.

Note:
- `upstream` refers to the powerbot copy of the repository
- `origin` refers to your copy of the repository

### Setting Up
1. Fork this repository
2. Add the remote
    - `git remote add origin https://github.com/[your github name]/powerbot.git`
3. Add the upstream source
    - `git remote add upstream https://github.com/powerbot/powerbot.git`
4. Change your working branch to the 'scripts' branch
    - `git checkout scripts`
5. Pull
	- `git pull upstream scripts`
    
### Making Changes
1. Ensure everything is up-to-date
	- `git checkout scripts`
	- `git pull upstream scripts`
1. Make a new branch
	- `git checkout -b [your branch name]`
	- All of your changes will be committed on this branch
1. Commit your changes
	- `git commit -a -m "Your update message"`
1. Push the branch to your repository
    - `git push -u origin [your branch name]`
1. Make a Pull Request from your forked repository
1. After your Pull Request has been merged, update the base and delete your branch
	- `git checkout scripts`
	- `git pull upstream scripts`
	- `git push -u origin scripts`
	- `git branch -d [your branch name]`

### Guidelines for Scripts
- Scripts **must** be packaged
- The parent package **must** be your powerbot username (e.g. coma/scripts/Script.java)
- Scripts **must** have an `author` tag in the script `Properties` with your powerbot username
- Scripts **must** have a `topic` tag with a thread ID corresponding to your script thread in the [Projects](http://www.powerbot.org/community/forum/55-projects/) section
- Pull requests should originate from a separate branch to ensure clean merges
- Failure to meet any of these guidelines will result in a denied pull request