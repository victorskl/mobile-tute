# Week 7 - Azure Backend Tutorial

Note on how to build Azure Mobile App Backend from the tutor slide. 

*Note: the related frontend Android application is in [Week 8](https://github.com/victorskl/mobile-tute/tree/master/week8).*

## Quickstart Todo App Service

1. Create SQL Server instance

2. Create SQL Database

    2.1 Note that multiple databases can be created in one SQL Server instance
    
3. Create App Service Plan instance (i.e. a VM that run IIS web server with .NET/C#)

4. Create **App Service > Mobile App > Android** using above resources
    
    4.1 Note that multiple App Services (.NET webapp) can be created in one App Service Plan instance

5. Download C# skeleton backend code i.e. the quickstart todo project [VictorTodo_Runtime.zip](VictorTodo_Runtime.zip) 

6. Open with Visual Studio 2017. Right click on `VictorTodoService` > `Manage NuGet Packages...` > `Updates` tab > select all and pull all dependencies. This will create `packages` directory and place all dependencies in there.

7. Next, open **Tool > NuGet Package Manager > Package Manager Console**

8. At PM:
    ```
    PM> Install-Package EntityFramework
    Package 'EntityFramework.6.1.3' already exists in project 'VictorTodoService'
    Time Elapsed: 00:00:00.8915935
    PM> Enable-Migrations
    Checking if the context targets an existing database...
    Code First Migrations enabled for project VictorTodoService.
    PM> Add-Migration
    cmdlet Add-Migration at command pipeline position 1
    Supply values for the following parameters:
    Name: InitialModel
    Scaffolding migration 'InitialModel'.
    The Designer Code for this migration file includes a snapshot of your current
    Code First model. This snapshot is used to calculate the changes to your
    model when you scaffold the next migration. If you make additional changes 
    to your model that you want to include in this migration, then you can 
    re-scaffold it by running 'Add-Migration InitialModel' again.
    ```
    
    ![nuget_entityframework_codefirst_migration.png](https://www.dropbox.com/s/xfmril1y5qs6t7o/nuget_entityframework_codefirst_migration.png?raw=1)

- **Rebuild** the solution, i.e  **Build > Rebuild Solution**

- **Publish** the Todo artifact to Azure App Service, i.e. at Visual Studio solution explorer, right click on the node **VictorTodoService > Publish**. Note that, tick the **Execute Code First Migrations** checkbox at the settings dialog and save it.

    ![exec_code_first_migrations.png](https://www.dropbox.com/s/b6mqz4vxy5x70lb/exec_code_first_migrations.png?raw=1)

- To practice more on *Entity Framework Code First Migrations*
    - https://msdn.microsoft.com/en-us/library/jj591621.aspx
    - https://msdn.microsoft.com/en-us/library/jj554735.aspx

- To study more about Azure App Service Backend trail
    - https://docs.microsoft.com/en-us/azure/app-service-mobile/app-service-mobile-dotnet-backend-how-to-use-server-sdk 

## Caveats

### The `MigrateDatabaseToLatestVersion` Mystery

- As you publish the artifact, you might expect the target database be populated with the necessary table. However, after published, you directly query to the database and find that **no table get populated!** Do not worry, it will. Just download the Android Client Application and run it. That then, the tables will get generated on the first Android Client query to the Backend App Service.

- But why, how??? Read on.

- https://msdn.microsoft.com/en-us/enus/library/dd394698#efcfmigrations
    (expand section *Configuring Database Deployment in Visual Studio* and read this.)
    > If you are deploying a web application project, Visual Studio can automate the process of deploying a database that is managed by Code First Migrations. When you configure the publish profile, you select a check box that is labeled Execute Code First Migrations (runs on application start):
    
    > This check box is shown for databases that the application accesses by using an Entity Framework Code First context class. When you select this option, the deployment process automatically configures the application `Web.config` file on the destination server so that Code First uses the `MigrateDatabaseToLatestVersion` initializer class.

- https://msdn.microsoft.com/en-us/magazine/dn818489.aspx

    > When you publish a Web app to Azure, there’s a checkbox on the settings page that’s unchecked by default. But that checkbox says “Execute code first migrations (runs on application start).”  Checking this will add the `MigrateDatabaseToLatestVersion` setting declaratively in the config file.


- What happen there is that, when you tick the **Execute Code First Migrations** during publishing, at the Azure App Service Plan (i.e. the IIS/.NET/C# web server VM that run our artifact), it will configure the `Web.config` (which is the deployment descriptor) for the `MigrateDatabaseToLatestVersion` setting during the **deploy-time**. Therefore, this migration and table population becomes implicit to you. And feel like it happens auto-magically!

- Therefore, you do not require to configure `MigrateDatabaseToLatestVersion` as described in, for example, in [this article][1] or using `DbMigrator` during App start up as in [this article][2].

- Also note that, the `DbMigrator` is useful for during the rapid development phase. However, this is not necessary code fragment to be deployed into Azure for production, in a sense. The `DbMigrator` is a developer convenience for running the `Update-Database` command for `EntityFramework` in NuGet Package Manager(PM) Console. For example, continue from the *Step 8 of Quickstart* section:

    ```
    PM> Install-Package EntityFramework
    Package 'EntityFramework.6.1.3' already exists in project 'VictorTodoService'
    Time Elapsed: 00:00:00.8915935
    PM> Enable-Migrations
    Checking if the context targets an existing database...
    Code First Migrations enabled for project VictorTodoService.
    PM> Add-Migration
    cmdlet Add-Migration at command pipeline position 1
    Supply values for the following parameters:
    Name: InitialModel
    Scaffolding migration 'InitialModel'.
    The Designer Code for this migration file includes a snapshot of your current
    Code First model. This snapshot is used to calculate the changes to your
    model when you scaffold the next migration. If you make additional changes 
    to your model that you want to include in this migration, then you can 
    re-scaffold it by running 'Add-Migration InitialModel' again.
    ...
    ... (working on model)
    ...
    PM> Add-Migration MyModifiedModel
    PM> Update-Database -Verbose
    ```
    
- In fact, you do not want to go back and ford with PM Console for this, then the `DbMigrator` is a convenience utility facade for you. 

- However, by default, running the `Update-Database` from PM Console target to the local database that configured at [`Web.config`](VictorTodoService/Web.config) > `connectionStrings` > `MS_TableConnectionString`. You will have to manually update this to point to the Azure database instance if you like it to apply to the (remote) Azure database instance. This is **not recommended** for a couple of reasons:
    - consider Azure database instance as a production environment
    - should develop against the local database for the dev purpose
    - expose the remote database connection string i.e. username, password
    - be careful, not to commit these DB connection string into Git


[1]: http://www.entityframeworktutorial.net/code-first/automated-migration-in-code-first.aspx

[2]: https://adrianhall.github.io/develop-mobile-apps-with-csharp-and-azure/chapter3/server/