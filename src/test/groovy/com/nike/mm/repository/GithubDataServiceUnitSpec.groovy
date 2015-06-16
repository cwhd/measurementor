package com.nike.mm.repository

import spock.lang.Specification;

class GithubDataServiceUnitSpec extends Specification {
	
	/*
	 * https://api.github.com/repos/cwhd/measurementor/commits?access_token=d23591f13c149fe29a1135f484cd28540cd3f7e4&page=<page_number_starting_at_one>&per_page=<amount_per_page>
	 *
		[
			{
				 "sha": "ef31c07b9bc78978271ebff73400ca42149c167e"
				,"commit": {
			    	"committer": {
			    		"name": "cdav18",
			       		"email": "chris.davis@nike.com",
			        	"date": "2015-03-29T14:56:42Z"
			      	}
			      	,"message": "github data mostly works, updating the setup script"
			      	,"comment_count": 0
			    },
			    "url": "https://api.github.com/repos/cwhd/measurementor/commits/ef31c07b9bc78978271ebff73400ca42149c167e",
			    "committer": {
			      "login": "cwhd",
			      "id": 1490140,
			      "url": "https://api.github.com/users/cwhd",
			      "type": "User",
			      "site_admin": false
			    }
			}
			      "site_admin": false
			    }
			}
		]
	 */
	
	/*
	 * https://api.github.com/users/reasonthearchitect/repos?access_token=d23591f13c149fe29a1135f484cd28540cd3f7e4&page=2&per_page=2
		[
		  {
		    "id": 24778301,
		    "name": "docker-java-discovery",
		    "full_name": "reasonthearchitect/docker-java-discovery",
		    "owner": {
		      "login": "reasonthearchitect",
		      "id": 4556453,
		      "avatar_url": "https://avatars.githubusercontent.com/u/4556453?v=3",
		      "gravatar_id": "",
		      "url": "https://api.github.com/users/reasonthearchitect",
		      "html_url": "https://github.com/reasonthearchitect",
		      "followers_url": "https://api.github.com/users/reasonthearchitect/followers",
		      "following_url": "https://api.github.com/users/reasonthearchitect/following{/other_user}",
		      "gists_url": "https://api.github.com/users/reasonthearchitect/gists{/gist_id}",
		      "starred_url": "https://api.github.com/users/reasonthearchitect/starred{/owner}{/repo}",
		      "subscriptions_url": "https://api.github.com/users/reasonthearchitect/subscriptions",
		      "organizations_url": "https://api.github.com/users/reasonthearchitect/orgs",
		      "repos_url": "https://api.github.com/users/reasonthearchitect/repos",
		      "events_url": "https://api.github.com/users/reasonthearchitect/events{/privacy}",
		      "received_events_url": "https://api.github.com/users/reasonthearchitect/received_events",
		      "type": "User",
		      "site_admin": false
		    },
		    "private": false,
		    "html_url": "https://github.com/reasonthearchitect/docker-java-discovery",
		    "description": "",
		    "fork": false,
		    "url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery",
		    "forks_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/forks",
		    "keys_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/keys{/key_id}",
		    "collaborators_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/collaborators{/collaborator}",
		    "teams_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/teams",
		    "hooks_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/hooks",
		    "issue_events_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/issues/events{/number}",
		    "events_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/events",
		    "assignees_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/assignees{/user}",
		    "branches_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/branches{/branch}",
		    "tags_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/tags",
		    "blobs_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/git/blobs{/sha}",
		    "git_tags_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/git/tags{/sha}",
		    "git_refs_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/git/refs{/sha}",
		    "trees_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/git/trees{/sha}",
		    "statuses_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/statuses/{sha}",
		    "languages_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/languages",
		    "stargazers_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/stargazers",
		    "contributors_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/contributors",
		    "subscribers_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/subscribers",
		    "subscription_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/subscription",
		    "commits_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/commits{/sha}",
		    "git_commits_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/git/commits{/sha}",
		    "comments_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/comments{/number}",
		    "issue_comment_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/issues/comments{/number}",
		    "contents_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/contents/{+path}",
		    "compare_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/compare/{base}...{head}",
		    "merges_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/merges",
		    "archive_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/{archive_format}{/ref}",
		    "downloads_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/downloads",
		    "issues_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/issues{/number}",
		    "pulls_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/pulls{/number}",
		    "milestones_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/milestones{/number}",
		    "notifications_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/notifications{?since,all,participating}",
		    "labels_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/labels{/name}",
		    "releases_url": "https://api.github.com/repos/reasonthearchitect/docker-java-discovery/releases{/id}",
		    "created_at": "2014-10-04T00:54:00Z",
		    "updated_at": "2014-10-04T01:13:00Z",
		    "pushed_at": "2014-10-04T01:12:59Z",
		    "git_url": "git://github.com/reasonthearchitect/docker-java-discovery.git",
		    "ssh_url": "git@github.com:reasonthearchitect/docker-java-discovery.git",
		    "clone_url": "https://github.com/reasonthearchitect/docker-java-discovery.git",
		    "svn_url": "https://github.com/reasonthearchitect/docker-java-discovery",
		    "homepage": null,
		    "size": 100,
		    "stargazers_count": 0,
		    "watchers_count": 0,
		    "language": "Groovy",
		    "has_issues": true,
		    "has_downloads": true,
		    "has_wiki": true,
		    "has_pages": false,
		    "forks_count": 0,
		    "mirror_url": null,
		    "open_issues_count": 0,
		    "forks": 0,
		    "open_issues": 0,
		    "watchers": 0,
		    "default_branch": "master",
		    "permissions": {
		      "admin": true,
		      "push": true,
		      "pull": true
		    }
		  },
		  {
		    "id": 24779600,
		    "name": "dockerfiles",
		    "full_name": "reasonthearchitect/dockerfiles",
		    "owner": {
		      "login": "reasonthearchitect",
		      "id": 4556453,
		      "avatar_url": "https://avatars.githubusercontent.com/u/4556453?v=3",
		      "gravatar_id": "",
		      "url": "https://api.github.com/users/reasonthearchitect",
		      "html_url": "https://github.com/reasonthearchitect",
		      "followers_url": "https://api.github.com/users/reasonthearchitect/followers",
		      "following_url": "https://api.github.com/users/reasonthearchitect/following{/other_user}",
		      "gists_url": "https://api.github.com/users/reasonthearchitect/gists{/gist_id}",
		      "starred_url": "https://api.github.com/users/reasonthearchitect/starred{/owner}{/repo}",
		      "subscriptions_url": "https://api.github.com/users/reasonthearchitect/subscriptions",
		      "organizations_url": "https://api.github.com/users/reasonthearchitect/orgs",
		      "repos_url": "https://api.github.com/users/reasonthearchitect/repos",
		      "events_url": "https://api.github.com/users/reasonthearchitect/events{/privacy}",
		      "received_events_url": "https://api.github.com/users/reasonthearchitect/received_events",
		      "type": "User",
		      "site_admin": false
		    },
		    "private": false,
		    "html_url": "https://github.com/reasonthearchitect/dockerfiles",
		    "description": "Contains the various Dockerfile definitions I'm maintaining.",
		    "fork": true,
		    "url": "https://api.github.com/repos/reasonthearchitect/dockerfiles",
		    "forks_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/forks",
		    "keys_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/keys{/key_id}",
		    "collaborators_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/collaborators{/collaborator}",
		    "teams_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/teams",
		    "hooks_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/hooks",
		    "issue_events_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/issues/events{/number}",
		    "events_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/events",
		    "assignees_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/assignees{/user}",
		    "branches_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/branches{/branch}",
		    "tags_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/tags",
		    "blobs_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/git/blobs{/sha}",
		    "git_tags_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/git/tags{/sha}",
		    "git_refs_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/git/refs{/sha}",
		    "trees_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/git/trees{/sha}",
		    "statuses_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/statuses/{sha}",
		    "languages_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/languages",
		    "stargazers_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/stargazers",
		    "contributors_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/contributors",
		    "subscribers_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/subscribers",
		    "subscription_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/subscription",
		    "commits_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/commits{/sha}",
		    "git_commits_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/git/commits{/sha}",
		    "comments_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/comments{/number}",
		    "issue_comment_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/issues/comments{/number}",
		    "contents_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/contents/{+path}",
		    "compare_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/compare/{base}...{head}",
		    "merges_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/merges",
		    "archive_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/{archive_format}{/ref}",
		    "downloads_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/downloads",
		    "issues_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/issues{/number}",
		    "pulls_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/pulls{/number}",
		    "milestones_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/milestones{/number}",
		    "notifications_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/notifications{?since,all,participating}",
		    "labels_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/labels{/name}",
		    "releases_url": "https://api.github.com/repos/reasonthearchitect/dockerfiles/releases{/id}",
		    "created_at": "2014-10-04T02:08:35Z",
		    "updated_at": "2014-10-04T02:46:35Z",
		    "pushed_at": "2014-10-04T04:09:14Z",
		    "git_url": "git://github.com/reasonthearchitect/dockerfiles.git",
		    "ssh_url": "git@github.com:reasonthearchitect/dockerfiles.git",
		    "clone_url": "https://github.com/reasonthearchitect/dockerfiles.git",
		    "svn_url": "https://github.com/reasonthearchitect/dockerfiles",
		    "homepage": null,
		    "size": 109,
		    "stargazers_count": 0,
		    "watchers_count": 0,
		    "language": "Shell",
		    "has_issues": false,
		    "has_downloads": true,
		    "has_wiki": true,
		    "has_pages": false,
		    "forks_count": 0,
		    "mirror_url": null,
		    "open_issues_count": 0,
		    "forks": 0,
		    "open_issues": 0,
		    "watchers": 0,
		    "default_branch": "master",
		    "permissions": {
		      "admin": true,
		      "push": true,
		      "pull": true
		    }
		  }
		]
	 */
	def "try to connect to github"() {
		
	}
	


}
