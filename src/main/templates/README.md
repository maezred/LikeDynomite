LikeDynomite
============

**${project.description}**

> Plugin for Bukkit/Spigot (a mod for Minecraft).
>
> Compatible with **1.11+** (1.11.1/1.11.2), **1.10** (1.10.1/1.10.2), and **1.9** (1.9.1/1.9.2/1.9.3/1.9.4)
>
> Latest Release: ${project.version} (${project.ext.buildDateTime}) ([Jump To Downloads](#downloads))

**Please report all issues on [GitHub](https://github.com/moltendorf/${project.name}/issues), thank you!**

## Overview

${project.name} tweaks the way TNT explodes:

  - When TNT is explodes near other TNT, it's primed at a configurable rate
    - Default is enabled with 25 TNT primed per second
  - TNT can be configured to not damage the environment
    - Default is enabled
  - TNT can be placed and ignited in protected regions (combine with previous option for some fun)
    - Default is also enabled (isn't that the point of this?)
  - TNT can instantly explode; skipping the priming phase
    - Default is enabled if the TNT is dense.


All features are fully configurable.

Source code is available on [GitHub](https://github.com/moltendorf/${project.name}).

Like what I've made? Support me by giving me a star, following me, or feel free to send me money [here]
(https://www.paypal.me/moltendorf). Thanks!

## Downloads

${project.name} is available for download at the following locations:

  - [Jenkins](https://moltendorf.net/ci/job/${project.ext.artifactId}/) ([Latest Successful Build](https://moltendorf.net/ci/job/${project.ext.artifactId}/lastSuccessfulBuild/))
  - [Releases](https://moltendorf.net/nexus/releases/net/moltendorf/bukkit/${project.ext.artifactId}/) ([Latest](https://moltendorf.net/nexus/latest/releases/)) | [Snapshots](https://moltendorf.net/nexus/snapshots/net/moltendorf/bukkit/${project.ext.artifactId}/) ([Latest](https://moltendorf.net/nexus/latest/snapshots/))
