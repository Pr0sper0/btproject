package sbr.track.btsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbr.track.btsystem.domain.Backlog;
import sbr.track.btsystem.domain.Project;
import sbr.track.btsystem.exceptions.ProjectIdException;
import sbr.track.btsystem.repositories.BacklogRepository;
import sbr.track.btsystem.repositories.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId() != null) {
                project.setBacklog(
                        backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException(
                    "Project ID '" + project.getProjectIdentifier().toUpperCase() + "' Already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' Does not exist");
        }
        return project;
    }

    public Project findProjectByName(String projectName) {
        Project project = projectRepository.findByProjectName(projectName.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project name '" + projectName + "' Does not exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects() {

        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException(
                    "Cannot delete Project with id '" + projectId + "'. This Project does not exist");
        }

        projectRepository.delete(project);
    }

    // public Project updateProjectByIdentifier(String projectId) {
    // Project project =
    // projectRepository.findByProjectIdentifier(projectId.toUpperCase());

    // if (project == null) {
    // throw new ProjectIdException("Project ID '" + projectId + "' Does not
    // exist");
    // }

    // try {
    // project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
    // return projectRepository.save(project);
    // } catch (Exception e){
    // throw new ProjectIdException("Project ID '" + projectId + "' Does not
    // exist");
    // }

    // }
}