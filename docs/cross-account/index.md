## Cross-Account Access

The plugin can access secrets stored in other AWS accounts.

**Warning:** This is an experimental feature. It is not officially supported.

## Setup

For each secondary AWS account:

1. Create the [cross-account role](http://docs.aws.amazon.com/IAM/latest/UserGuide/tutorial_cross-account-with-roles.html) and associated policies in AWS.
2. Test that Jenkins can assume the role and retrieve secrets from the other account.
3. Add the role ARN to the `roles` list in the plugin configuration.

## Considerations

- **Do not add more AWS accounts than absolutely necessary.** Each additional AWS account necessitates another set of HTTP requests to retrieve secrets. This increases the time to populate the credential list. It also increases the risk of service degradation, as any of those requests could fail.

## Restrictions

- **The secret name must be unique** across all AWS accounts that Jenkins uses. The credential provider will stop working when duplicate secret names are present: if this happens, delete or rename the duplicates, and then retry the failed Jenkins operation.
