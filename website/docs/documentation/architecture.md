---
id: architecture
title: Architecture
---

## Architecture

This is a java library, which aggregates and extends the capabilities of
the underlying template engine and converter tools.
For the value replacement it currently only supports pojo value objects.

The engine aggregates components to parse, convert and merge documents, 
where these features are pluggable components to ensure long-term usability and 
customization. Thus, there is a built-in set of template engine wrappers and converters shipped with the engine with 
the ability of adding 3rd party building blocks.

![structure](assets/overview-key_elements.png)
