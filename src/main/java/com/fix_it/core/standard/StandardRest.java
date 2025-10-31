    package com.fix_it.core.standard;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import jakarta.validation.Valid;
    import java.util.List;

    public abstract class StandardRest<T, ID, R extends JpaRepository<T, ID>> {

        protected final R repository;

        public StandardRest(R repository) {
            this.repository = repository;
        }

        @GetMapping
        public ResponseEntity<List<T>> findAll() {
            List<T> entities = repository.findAll();
            return ResponseEntity.ok(entities);
        }

        @GetMapping("/{id}")
        public ResponseEntity<T> findById(@PathVariable ID id) {
            return repository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<T> create(@Valid @RequestBody T entity) {
            T savedEntity = repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
        }

        @PutMapping("/{id}")
        public ResponseEntity<T> update(@PathVariable ID id, @Valid @RequestBody T entity) {
            if (!repository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            T updatedEntity = repository.save(entity);
            return ResponseEntity.ok(updatedEntity);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable ID id) {
            if (!repository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }