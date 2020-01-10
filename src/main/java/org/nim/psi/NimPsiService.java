package org.nim.psi;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyKey;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.ContainerUtil;
import org.nim.psi.extension.NimModule;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class NimPsiService {

    private static final NotNullLazyKey<NimPsiService, Project> INSTANCE_KEY =
            ServiceManager.createLazyKey(NimPsiService.class);


    private final ConcurrentMap<GlobalSearchScope, Map<String, NimModule>> moduleCache =
            ContainerUtil.createConcurrentSoftKeySoftValueMap();

    private final ConcurrentMap<GlobalSearchScope, Map<String, NimTypeStructureDeclaration>> typeCache =
            ContainerUtil.createConcurrentSoftKeySoftValueMap();

    private final Project project;


    public NimPsiService(Project project) {
        this.project = project;

        final PsiModificationTracker modificationTracker = PsiManager.getInstance(project).getModificationTracker();

        project.getMessageBus().connect().subscribe(PsiModificationTracker.TOPIC, new PsiModificationTracker.Listener() {

            private long lastTimeSeen = -1L;

            @Override
            public void modificationCountChanged() {
               long now = modificationTracker.getModificationCount();
               if(lastTimeSeen != now){
                   lastTimeSeen = now;
                   moduleCache.clear();
               }
            }
        });
    }

    public Project getProject() {
        return project;
    }

    public static NimPsiService getInstance(Project project){
        return INSTANCE_KEY.getValue(project);
    }
}
